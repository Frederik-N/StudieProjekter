package main

import (
	"bufio"
	cryptrand "crypto/rand"
	"crypto/sha256"
	"encoding/gob"
	"encoding/json"
	"fmt"
	"math/big"
	mathrand "math/rand"
	"net"
	"os"
	"sort"
	"strconv"
	"strings"
	"sync"
	"time"
)

// Each block contains these variables
type Block struct {
	Seed            int
	Index           int
	Hash            [32]byte
	LastHash        [32]byte
	Peer            string
	Transactions    []SignedTransaction
	SpecialAccounts []*big.Int
}

// The blockchain which is all the blocks validated
type Blockchain struct {
	Root *Node
}
type Node struct {
	Children  []*Node
	BlockNode Block
}

// insert block to blockchain (Only if lasthash matches the match of some block of a leaf in the blockchain)
func (n *Node) insert(block Block) {
	if block.LastHash == n.BlockNode.Hash {
		n.Children = append(n.Children, &Node{Children: nil, BlockNode: block})
	} else if n.Children != nil {
		for _, child := range n.Children {
			child.insert(block)
		}
	}
}

// get all blocks in the longest blockchain
func getblockchain() []Block {
	Intblock = *new(IntBlock)
	allchains(Mainblockchain.Root, 0)
	var bestsofar = 0
	var lastindex = 0
	var node []Block
	for index, c := range Intblock.clist {
		if bestsofar <= c {
			bestsofar = c
			lastindex = index
		}
	}
	for i := 0; i <= bestsofar; i++ {
		node = append([]Block{Intblock.blist[lastindex+i]}, node...)
	}
	return node
}

// Check if the block already is in the blockchain
func (n *Node) isInblockchain(block Block) bool {
	if block.Hash == n.BlockNode.Hash {
		return true
	} else {
		for _, child := range n.Children {
			if child.isInblockchain(block) {
				return true
			}
		}
	}
	return false
}

type IntBlock struct {
	clist []int
	blist []Block
}

var Intblock IntBlock
var FinishedTransactions []SignedTransaction

//Fills Intblock with each branch in the blockchain
func allchains(n *Node, counter int) {
	var c = counter
	if n.Children != nil {
		for _, child := range n.Children {
			allchains(child, c+1)
		}
	}
	Intblock.clist = append(Intblock.clist, c)
	Intblock.blist = append(Intblock.blist, n.BlockNode)

}

// gets the longest chain from the blockchain
func longestchain(intBlock IntBlock) (int, Block) {
	var bestsofar = 0
	var node Block
	for index, c := range intBlock.clist {
		if bestsofar <= c {
			bestsofar = c
			node = intBlock.blist[index]
		}
	}
	return bestsofar, node
}

var specialAccounts []*big.Int

type SignedBlock struct {
	Signature  string
	DrawSigned DrawSigned
	Keyn       *big.Int
	Block      Block
}

type DrawSigned struct {
	Draw      Draw
	Signature string
}
type Draw struct {
	Lottery string
	Seed    int
	Slot    int
}

var connections []net.Conn
var transactionCheck map[string]bool
var transactionBuffer map[string]SignedTransaction
var mutexCheck, mutexCheck2, mutexBuff, mutexBlock, mutexBlock2, mutexBlock3, mutexID = new(sync.Mutex), new(sync.Mutex), new(sync.Mutex), new(sync.Mutex), new(sync.Mutex), new(sync.Mutex), new(sync.Mutex)
var connDial net.Conn
var ledger *Ledger
var peerList *Network
var connectedCount = 0
var err error
var ownIP string

type Network struct {
	ConnectedNetwork []string
}

type Ledger struct {
	Accounts map[string]int
	lock     sync.Mutex
}

func (l *Ledger) SignedTransaction(t *SignedTransaction) {
	l.lock.Lock()
	defer l.lock.Unlock()

	//Get the n from From to use it to verify signature
	nFrom := new(big.Int)
	nFrom.SetString(t.From, 0)
	nFrom.Sub(nFrom, e)
	validSignature := verify(t, e, nFrom)

	if validSignature {
		if l.Accounts[t.From] < t.Amount {
			fmt.Println("NOT ENOUGH MONEY ON THE ACCOUNT")
			return
		}
		l.Accounts[t.From] -= t.Amount
		fmt.Println("From, updated account:", l.Accounts[t.From])
		l.Accounts[t.To] += t.Amount - 1
		fmt.Println("To ", t.To, ", updated account:", l.Accounts[t.To])
	}
}

// reward block peer
func (l *Ledger) rewardBlockPeer(len int, peer string) {
	l.lock.Lock()
	defer l.lock.Unlock()
	newInt := new(big.Int)
	newInt.SetString(peer, 0)
	newInt.Add(newInt, e)
	l.Accounts[newInt.String()] += len + 10
}

func MakeLedger() *Ledger {
	ledger := new(Ledger)
	ledger.Accounts = make(map[string]int)
	return ledger
}

type SignedTransaction struct {
	ID        string
	From      string
	To        string
	Amount    int
	Signature string
}

type Message struct {
	MessageType       string
	MyIP              string
	SignedBlock       SignedBlock
	SignedTransaction SignedTransaction
}

var e = big.NewInt(int64(3))
var n *big.Int
var d *big.Int
var ndmap map[*big.Int]*big.Int
var Mainblockchain Blockchain
var RewardedBlocks []Block

func main() {
	mathrand.Seed(time.Now().UnixNano())
	transactionCheck = map[string]bool{}
	transactionBuffer = map[string]SignedTransaction{}
	connections = []net.Conn{}
	peerList = new(Network)
	peerList.ConnectedNetwork = []string{}
	Mainblockchain = Blockchain{}

	reader := bufio.NewReader(os.Stdin)
	fmt.Print("Please write IP-address: ")
	ipText, _ := reader.ReadString('\n')
	ipText = strings.TrimSpace(ipText)
	fmt.Println("You're trying to connect to ip: ", ipText)

	fmt.Print("Input port here: ")
	port, _ := reader.ReadString('\n')
	port = strings.TrimSpace(port)

	ipPort := ipText + ":" + port

	//Create random portNr for the peer to listen on
	portServer := strconv.Itoa(mathrand.Intn(65500-49000) + 49000)
	portServer = strings.TrimSpace(portServer)

	fmt.Println("This servers port is: ", portServer)
	listener, _ := net.Listen("tcp", ":"+portServer)
	ownIP = strings.TrimSpace("127.0.0.1:" + portServer)
	// Try to connect to server
	connDial, _ = net.Dial("tcp", ipPort)

	ledger = MakeLedger()
	var seed = 0
	if connDial == nil {
		// Initial seed is picked
		t := time.Now()
		mathrand.Seed(t.UnixNano())
		seed = int(t.UnixNano())
		ndmap = make(map[*big.Int]*big.Int)

		// generate 10 public keys with 10^6 AUs on them
		var specialAccounts []*big.Int
		for i := 1; i <= 10; i++ {
			n, d = keyGen(2000, e)

			// each secret key is hardcoded in the map.
			ndmap[n] = d
			specialAccounts = append(specialAccounts, n)

			account := new(big.Int)
			account = account.Add(n, e)

			ledger.Accounts[account.String()] += 1000000
		}

		// Genesis block is created with ten special public keys of 10^6 AUs
		genesisBlock := Block{}
		genesisBlock = Block{seed, 0, blockHash(genesisBlock), [32]byte{}, "", []SignedTransaction{}, specialAccounts}
		fmt.Print("ledger: ") // test to see ledger
		fmt.Println(ledger)   // test to see ledger
		Mainblockchain = Blockchain{}
		Mainblockchain.Root = &Node{nil, genesisBlock}
		fmt.Print("Mainblockchain.Root: ") // test to see genesisblock
		fmt.Println(Mainblockchain.Root)
		println("Connecting to own port")
		connDial, err = net.Dial("tcp", "127.0.0.1:"+portServer)
		if err != nil {
			fmt.Println("Error in Dialing to own connection", err.Error())
			return
		}
	} else {
		n, d = keyGen(2000, e)
		fmt.Println("n: ", n)
		// We connected, now wait until we recieve their network and the genesisblock
		network := &Network{}
		genesisBlock := &Node{}
		dec := gob.NewDecoder(connDial)
		err := dec.Decode(&network)
		err2 := dec.Decode(&genesisBlock)
		connDial.Close()
		if err != nil || err2 != nil {
		}
		// save the genesisBlock to your blockchain
		Mainblockchain = Blockchain{}
		Mainblockchain.Root = &Node{nil, genesisBlock.BlockNode}
		fmt.Print("Mainblockchain.Root: ")
		fmt.Println(Mainblockchain.Root)
		// get seed from genesisBlock
		seed = Mainblockchain.Root.BlockNode.Seed

		// Update Ledger with specialaccounts
		for _, account := range Mainblockchain.Root.BlockNode.SpecialAccounts {
			ledger.Accounts[new(big.Int).Add(account, e).String()] += 1000000
		}

		// Add the recieved network to peerlist
		peerList.ConnectedNetwork = append(peerList.ConnectedNetwork, network.ConnectedNetwork...)

		// Add ourself to the peerlist if not already in it

		peerList.ConnectedNetwork = append(peerList.ConnectedNetwork, "127.0.0.1:"+portServer)

		sort.Strings(peerList.ConnectedNetwork)
		for _, conn := range connections {
			conn.Close()
		}
		connections = []net.Conn{}
		//check placement in peerlist
		if len(connections) < 3 {
			placementInPeerList := 0
			for i, conpeer := range peerList.ConnectedNetwork {
				if ownIP == conpeer {
					placementInPeerList = i
				}
			}
			//connect to the next x peers in peerlist

			for i := 1; i <= 3; i++ {
				if len(connections) < 3 {
					if ((placementInPeerList + i) % len(peerList.ConnectedNetwork)) == placementInPeerList {
						break
					}
					connDial, _ := net.Dial("tcp", peerList.ConnectedNetwork[((placementInPeerList+i)%len(peerList.ConnectedNetwork))]) // connect to the sorted received network
					connections = append(connections, connDial)
					go handleConnection(connDial)
				} else {
					break
				}
			}
			for _, connection := range connections {
				// For each new connection we make, send a message with our IP
				ipBroadcast := Message{}
				ipBroadcast.MessageType = "ip"
				ipBroadcast.MyIP = "127.0.0.1:" + portServer
				dec := gob.NewEncoder(connection)
				err := dec.Encode(&ipBroadcast)
				if err != nil {
				}

			}
		}
	}
	if len(peerList.ConnectedNetwork) == 0 {
		peerList.ConnectedNetwork = append(peerList.ConnectedNetwork, "127.0.0.1:"+portServer)
	}
	sort.Strings(peerList.ConnectedNetwork)
	fmt.Println("Waiting for connection on 127.0.0.1:", portServer)

	// initialize slot number
	var slot = 0

	//Client input handling
	go input()
	go handleConnection(connDial)

	defer listener.Close()

	// Do transactions from the blockchain every 20 sec
	go func() {
		for {
			time.Sleep(20 * time.Second)
			var blockslist = getblockchain()
			fmt.Print("longest blockchainsize ")
			fmt.Println(len(blockslist))
			for _, blockinchain := range blockslist {
				var blockpeer = blockinchain.Peer
				newTransactions := []SignedTransaction{}
				for _, transaction := range blockinchain.Transactions {
					var alreadyDone = false
					for _, finishedtransaction := range FinishedTransactions {
						if finishedtransaction.ID == transaction.ID {
							alreadyDone = true
						}
					}
					// Do transaction if not already done.
					if transaction.Amount != 0 && alreadyDone == false {
						ledger.SignedTransaction(&transaction)
						newTransactions = append(newTransactions, transaction)
					}
				}
				//Transaction that is done.
				FinishedTransactions = append(FinishedTransactions, newTransactions...)

				var alreadyRewarded = false
				// check if any peer needs reward for adding block to blockchain
				for _, RewardedBlock := range RewardedBlocks {
					if blockinchain.Hash == RewardedBlock.Hash {
						alreadyRewarded = true
					}
				}
				if !alreadyRewarded {
					ledger.rewardBlockPeer(len(newTransactions), blockpeer)
					fmt.Println("reward peer " + blockpeer)
					fmt.Print("New Balance: ")
					newInt := new(big.Int)
					newInt.SetString(blockpeer, 0)
					newInt.Add(newInt, e)
					fmt.Println(ledger.Accounts[newInt.String()])

				}
				RewardedBlocks = append(RewardedBlocks, blockinchain)
			}
			fmt.Print("transactions done: ")
			fmt.Println(len(FinishedTransactions))
		}
	}()

	// begin the lottery every 1 sec
	go func() {
		for {
			// check if enough connections every 5 sec
			time.Sleep(5 * time.Second)
			var length = len(connections)
			// set "number" to the amount of peers that has to connect before start. length >= number
			if ndmap != nil && length >= 1 {
				for {
					time.Sleep(1 * time.Second)

					// make new block to add to blockchain if win
					mutexBlock.Lock()
					//use the last block of longest chain to make newblock
					Intblock = *new(IntBlock)
					allchains(Mainblockchain.Root, 0)
					var _, lastBlock = longestchain(Intblock)
					mutexBlock.Unlock()
					fmt.Println("Begins lottery slot: " + strconv.Itoa(slot))
					for nmap, dmap := range ndmap {
						var transactions []SignedTransaction
						var blocktrans []SignedTransaction
						for _, transaction := range transactionBuffer {
							transactions = append(transactions, transaction)
						}
						for _, trans := range transactions {
							newtrans := true
							for _, finishedtrans := range FinishedTransactions {
								if trans.ID == finishedtrans.ID {
									newtrans = false
								}
							}
							if newtrans {
								blocktrans = append(blocktrans, trans)
							}
						}
						newBlock := newBlock(lastBlock, seed, nmap.String(), blocktrans)
						if lotteryWon(slot, dmap, seed, nmap) {
							mutexBlock.Lock()
							// Insert the new block to the blockchain
							Mainblockchain.Root.insert(newBlock)
							mutexBlock.Unlock()

							// Send SignedDraw to all peers. They can verify this is winner using verifyDraw
							var drawSigned = getSignatureDraw(Draw{"LOTTERY", seed, slot}, dmap, nmap)
							// Send Signed(newblock) to all peers
							var blockSignature = getSignatureBlock(newBlock, dmap, nmap)

							// For each connection send out the signedDraw and the signed block
							message := Message{}
							message.MessageType = "block"
							signedblock := SignedBlock{}
							message.SignedBlock = signedblock
							message.SignedBlock.Signature = blockSignature
							message.SignedBlock.DrawSigned = DrawSigned{Draw{"LOTTERY", seed, slot}, drawSigned}
							message.SignedBlock.Keyn = nmap
							message.SignedBlock.Block = newBlock
							sendMessage(message)
						}
					}
					slot = slot + 1
				}
			}
		}
	}()

	//Server handling, keep listening for connections.
	for {
		connIncoming, err := listener.Accept()
		if err != nil {
			fmt.Println("Error in incoming connection", err.Error())
			return
		}
		enc := gob.NewEncoder(connIncoming)
		enc.Encode(peerList)

		// if this is the peer that initialized the genesisblock, send it out.
		if ndmap != nil {
			enc.Encode(Mainblockchain.Root)
		}
		go handleConnection(connIncoming)
		defer connIncoming.Close()
	}

}

// begins the lottery and chooses a winner out of all the stakeholders in lotteryBlocks
func lotteryWon(slot int, d *big.Int, seed int, n *big.Int) bool {

	var draw = getSignatureDraw(Draw{"LOTTERY", seed, slot}, d, n)
	var a = big.NewInt(int64(1000000))

	var val = a.Mul(a, calculateH("LOTTERY", seed, slot, n, draw))

	// setup hardness
	var bytes = []byte{14, 254, 25, 47, 168, 142, 151, 44, 64, 169, 66, 22, 115, 60, 181, 27, 3, 168, 232, 247, 103, 64, 178, 132, 83, 129, 11, 233, 218, 9, 131, 94, 12, 43, 4}
	var hardness = new(big.Int)
	hardness = hardness.SetBytes(bytes[:])

	// lottery won
	if val.Cmp(hardness) == 1 {
		return true
	}
	return false
}

func handleConnection(conn net.Conn) {
	//print connections properly
	/*
	 Keep listening on connection and if message hasn't been send before
	  send the message.
	*/
	dec := gob.NewDecoder(conn)

	message := Message{}
	for {
		//Decode the incoming transaction
		err := dec.Decode(&message)
		if err != nil {
		}

		switch msgType := message.MessageType; msgType {

		case "ip":
			handleIP(message)
		case "transaction":
			go handleTrans(message)
		case "block":
			go handleBlock(message)

		}
	}
}

func handleBlock(message Message) {
	//mutexBlock3.Lock()
	signedBlock := message.SignedBlock
	// check if block already added to blockchain
	if Mainblockchain.Root.isInblockchain(signedBlock.Block) {
		return
	}
	//verify the block and draw, and add the block to blockchain if it is verified
	if verifyBlock(signedBlock) {
		if verifyDraw(signedBlock.DrawSigned, signedBlock.Keyn) {
			for _, t := range signedBlock.Block.Transactions {
				nFrom := new(big.Int)
				nFrom.SetString(t.From, 0)
				nFrom.Sub(nFrom, e)
				if !verify(&t, e, nFrom) {
					return
				}
			}
			mutexBlock.Lock()
			Mainblockchain.Root.insert(signedBlock.Block)
			mutexBlock.Unlock()
		}
	}
	//mutexBlock3.Unlock()
}

func handleIP(message Message) {
	// Add that IP to our network
	if !ipInNetwork(message.MyIP, peerList) {
		peerList.ConnectedNetwork = append(peerList.ConnectedNetwork, message.MyIP)
		//sort peerlist
		sort.Strings(peerList.ConnectedNetwork)

		// Dial the network until we recieve 3 dialed servers
		//find own placement in peerlist and store index
		placementInPeerList := 0
		for i, conpeer := range peerList.ConnectedNetwork {
			if ownIP == conpeer {
				placementInPeerList = i
			}
		}
		//close all previous connections
		for _, conn := range connections {
			conn.Close()
		}
		connections = []net.Conn{}

		//connect to the next 3 ip's in peerlist after your own placement.
		for i := 1; i <= 3; i++ {
			if len(connections) < 3 {
				if ((placementInPeerList + i) % len(peerList.ConnectedNetwork)) == placementInPeerList {
					break
				}
				connDial, err := net.Dial("tcp", peerList.ConnectedNetwork[((placementInPeerList+i)%len(peerList.ConnectedNetwork))])
				if err == nil {
					connections = append(connections, connDial)
					go handleConnection(connDial)
				} else {
					fmt.Println(err.Error())
				}

			} else {
				break
			}
		}
	}
}

func handleTrans(message Message) {
	// Check if we have recieved this transaction already
	mutexCheck.Lock()
	if transactionCheck[message.SignedTransaction.ID] == false {
		sendMessage(message)
	}
	mutexCheck.Unlock()
}

func printConnections() {
	connect2 := []net.Addr{}
	for i := 0; i < len(connections); i++ {
		connect2 = append(connect2, connections[i].RemoteAddr())
	}
	fmt.Println("Own Connections handle ", connect2)
}
func isOneOfFirstXPeers(ip string, connectedNetwork *Network, x int) bool {

	for i := 0; i < x; i++ {
		if i > len(connectedNetwork.ConnectedNetwork) {
			return true
		}
		if ip == connectedNetwork.ConnectedNetwork[i] {
			return true
		}
	}
	return false
}

func remove(connections []net.Conn, i int) []net.Conn {
	return append(connections[:i], connections[i+1:]...)
}

func ipInNetwork(ip string, connectedNetwork *Network) bool {
	for _, conn := range connectedNetwork.ConnectedNetwork {
		if ip == conn {
			return true
		}
	}
	return false
}

func sendMessage(message Message) {
	// If we're sending an IP message, just send to everyone
	if message.MessageType == "transaction" {
		if transactionCheck[message.SignedTransaction.ID] == false {
			transactionCheck[message.SignedTransaction.ID] = true
			// First time receiving the transaction, add it to our buffer until we recieve a block
			mutexBuff.Lock()
			transactionBuffer[message.SignedTransaction.ID] = message.SignedTransaction
			mutexBuff.Unlock()
		}

	}

	//Sending the encoded transaction to all known connections
	for _, connection := range connections {
		enc := gob.NewEncoder(connection)
		err := enc.Encode(message)
		if err != nil {
		}
	}
}

func input() {
	reader := bufio.NewReader(os.Stdin)
	for {
		fmt.Print("Amount to send: ")
		amount, err := reader.ReadString('\n')
		amount = strings.TrimSpace(amount)

		if amount == "runtest" {
			go testLoop(big.NewInt(int64(9999999999999)))
			go testLoop(big.NewInt(int64(5555555555555)))
			continue
		}
		if amount == "printledger" {
			ledger.printLedger()
			continue
		}

		if err != nil || amount == "quit\r\n" {
			fmt.Println("Closing Your Connection")
			return
		}
		amountInt, _ := strconv.Atoi(amount)
		amountBigInt := big.NewInt(int64(amountInt))

		fmt.Print("To n: ")
		to, _ := reader.ReadString('\n')
		to = strings.TrimSpace(to)
		toBigInt := new(big.Int)
		toBigInt.SetString(to, 0)

		message := Message{}
		message.SignedTransaction = SignedTransaction{}
		message.SignedTransaction.Amount = amountInt
		// From is a combination of n and e
		from := new(big.Int)
		from = from.Add(n, e)
		message.SignedTransaction.From = from.String()
		// To is a combination of n and e
		to2 := toBigInt.Add(toBigInt, e)
		message.SignedTransaction.To = to2.String()
		// ID is a combination of from, to and a random number
		id := new(big.Int)
		id.Add(from, id.Add(to2, big.NewInt(int64(mathrand.Intn(1000)))))
		message.SignedTransaction.ID = id.String()
		// Signature consists of every field value signed and split by commas
		message.SignedTransaction.Signature = getSignature(amountBigInt, from, to2, id)
		message.MessageType = "transaction"
		mutexCheck.Lock()
		sendMessage(message)
		mutexCheck.Unlock()
	}
}

func keyGen(k int, e *big.Int) (*big.Int, *big.Int) {
	primeP, _ := cryptrand.Prime(cryptrand.Reader, k/2)
	primeQ, _ := cryptrand.Prime(cryptrand.Reader, k/2)
	n = new(big.Int).Mul(primeP, primeQ)

	primeMul := new(big.Int)
	primeP.Add(primeP, big.NewInt(-1))
	primeQ.Add(primeQ, big.NewInt(-1))
	primeMul.Mul(primeP, primeQ)

	d = new(big.Int).ModInverse(e, primeMul)
	//if true: Case where primeP and primeQ are GCD
	//if false: primeP and primeQ are not GCD try again
	if d != nil {
		return n, d
	} else {
		return keyGen(k, e)
	}
}

func sign(ciphertext *big.Int, d *big.Int, n *big.Int) *big.Int {
	temp := new(big.Int)
	temp.Exp(ciphertext, d, n)
	return temp
}

func verify(signedTrans *SignedTransaction, e *big.Int, n *big.Int) bool {
	// Getting the signed strings by their split
	signedStrings := strings.Split(signedTrans.Signature, ",")
	signedAmount, signedFrom, signedTo, signedID := signedStrings[0], signedStrings[1], signedStrings[2], signedStrings[3]

	// Check if amount is over 0
	if signedTrans.Amount <= 0 {
		fmt.Println("INVALID AMOUNT")
		return false
	}

	// Hash all the clean messages
	checkAmount, checkFrom, checkTo, checkID := new(big.Int), new(big.Int), new(big.Int), new(big.Int)
	checkAmount = big.NewInt(int64(signedTrans.Amount))
	checkFrom.SetString(signedTrans.From, 0)
	checkTo.SetString(signedTrans.To, 0)
	checkID.SetString(signedTrans.ID, 0)
	hash, hash2, hash3, hash4 := sha256.Sum256(checkAmount.Bytes()), sha256.Sum256(checkFrom.Bytes()), sha256.Sum256(checkTo.Bytes()), sha256.Sum256(checkID.Bytes())
	checkAmount, checkFrom, checkTo, checkID = checkAmount.SetBytes(hash[:]), checkFrom.SetBytes(hash2[:]), checkTo.SetBytes(hash3[:]), checkID.SetBytes(hash4[:])

	// Unsign the signed message and compare it to the hashed clean message. Repeat for all.
	unsignedAmount := new(big.Int)
	unsignedAmount.SetString(signedAmount, 0)
	unsignedAmount.Exp(unsignedAmount, e, n)
	if unsignedAmount.Cmp(checkAmount) != 0 {
		fmt.Println("FAILED AMOUNT CHECK")
		return false
	}

	unsignedFrom := new(big.Int)
	unsignedFrom.SetString(signedFrom, 0)
	unsignedFrom.Exp(unsignedFrom, e, n)
	if unsignedFrom.Cmp(checkFrom) != 0 {
		fmt.Println("FAILED FROM CHECK")
		return false
	}

	unsignedTo := new(big.Int)
	unsignedTo.SetString(signedTo, 0)
	unsignedTo.Exp(unsignedTo, e, n)
	if unsignedTo.Cmp(checkTo) != 0 {
		fmt.Println("FAILED TO CHECK")
		return false
	}

	unsignedID := new(big.Int)
	unsignedID.SetString(signedID, 0)
	unsignedID.Exp(unsignedID, e, n)
	if unsignedID.Cmp(checkID) != 0 {
		fmt.Println("FAILED ID CHECK")
		return false
	}

	// Passed all checks, we good
	return true
}

func verifyDraw(drawSigned DrawSigned, npeer *big.Int) bool {
	drawMarshal, _ := json.Marshal(drawSigned.Draw)
	// hash the clean messages
	shaDraw := sha256.Sum256(drawMarshal)
	hashedDraw := new(big.Int)
	hashedDraw = hashedDraw.SetBytes(shaDraw[:])

	// unsign and verify signedDraw
	unsignedDraw := new(big.Int)
	unsignedDraw.SetString(drawSigned.Signature, 0)
	unsignedDraw.Exp(unsignedDraw, e, npeer)
	if hashedDraw.Cmp(unsignedDraw) != 0 {
		return false
	}

	// verify signeddraw to check if it is lotterywinner
	var nIsSpecialAccount = false
	for _, element := range Mainblockchain.Root.BlockNode.SpecialAccounts {
		if npeer.Cmp(element) == 0 {
			nIsSpecialAccount = true
		}
	}
	if !nIsSpecialAccount {
		return false
	}
	var draw = drawSigned.Signature
	var a = big.NewInt(int64(1000000))

	var val = a.Mul(a, calculateH(drawSigned.Draw.Lottery, drawSigned.Draw.Seed, drawSigned.Draw.Slot, npeer, draw))

	var bytes = []byte{14, 254, 25, 47, 168, 142, 151, 44, 64, 169, 66, 22, 115, 60, 181, 27, 3, 168, 232, 247, 103, 64, 178, 132, 83, 129, 11, 233, 218, 9, 131, 94, 12, 43, 4}
	var hardness = new(big.Int)
	hardness = hardness.SetBytes(bytes[:])

	if val.Cmp(hardness) == 1 {
		return true
	}
	return false
}

func verifyBlock(signedBlock SignedBlock) bool {
	blockMarshal, _ := json.Marshal(signedBlock.Block)
	// hash the clean messages
	shaBlock := sha256.Sum256(blockMarshal)
	hashedBlock := new(big.Int)
	hashedBlock = hashedBlock.SetBytes(shaBlock[:])

	// unsign and verify signedblock
	unsignedBlock := new(big.Int)
	unsignedBlock.SetString(signedBlock.Signature, 0)
	unsignedBlock.Exp(unsignedBlock, e, signedBlock.Keyn)
	if hashedBlock.Cmp(unsignedBlock) != 0 {
		return false
	}

	return true
}

func getSignature(amount *big.Int, from *big.Int, to *big.Int, id *big.Int) string {
	// Hashing all the messages
	hash, hash2, hash3, hash4 := sha256.Sum256(amount.Bytes()), sha256.Sum256(from.Bytes()), sha256.Sum256(to.Bytes()), sha256.Sum256(id.Bytes())
	hashedAmount, hashedFrom, hashedTo, hashedID := new(big.Int), new(big.Int), new(big.Int), new(big.Int)
	hashedAmount, hashedFrom, hashedTo, hashedID = hashedAmount.SetBytes(hash[:]), hashedFrom.SetBytes(hash2[:]), hashedTo.SetBytes(hash3[:]), hashedID.SetBytes(hash4[:])
	// Signing all the messages
	signedAmount, signedFrom, signedTo, signedID := sign(hashedAmount, d, n), sign(hashedFrom, d, n), sign(hashedTo, d, n), sign(hashedID, d, n)
	return (signedAmount.String() + "," + signedFrom.String() + "," + signedTo.String() + "," + signedID.String())
}

func getSignatureDraw(draw Draw, d *big.Int, n *big.Int) string {
	drawmarshal, _ := json.Marshal(draw)
	// hash the draw
	drawSHA := sha256.Sum256(drawmarshal)

	// Signing the draw
	hashedDraw := new(big.Int)
	hashedDraw = hashedDraw.SetBytes(drawSHA[:])
	signed := sign(hashedDraw, d, n)
	return signed.String()
}

// hash block to its SHA256 representation
func blockHash(block Block) [32]byte {
	blockmarshal, _ := json.Marshal(block)
	sha := sha256.Sum256(blockmarshal)
	return sha
}

func getSignatureBlock(block Block, d *big.Int, n *big.Int) string {
	blockmarshal, _ := json.Marshal(block)
	// hash the block
	blockSHA := sha256.Sum256(blockmarshal)

	// Signing the block
	hashedBlock := new(big.Int)
	hashedBlock = hashedBlock.SetBytes(blockSHA[:])
	signedBlock := sign(hashedBlock, d, n)
	return signedBlock.String()
}

// new block creation, using blockHash
func newBlock(lastBlock Block, seed int, npeer string, transactions []SignedTransaction) Block {
	var newBlock Block

	newBlock.Seed = seed
	newBlock.Index = lastBlock.Index + 1
	newBlock.LastHash = lastBlock.Hash
	newBlock.Peer = npeer
	newBlock.SpecialAccounts = lastBlock.SpecialAccounts
	newBlock.Transactions = transactions
	newBlock.Hash = blockHash(newBlock)
	return newBlock
}

func calculateH(lottery string, seed int, slot int, n *big.Int, draw string) *big.Int {
	hString := lottery + string(seed) + string(slot) + draw

	h := sha256.New()
	h.Write([]byte(hString))
	h.Write(n.Bytes())
	hashed := h.Sum(nil)
	hashedh := new(big.Int)
	hashedh = hashedh.SetBytes(hashed[:])

	return hashedh
}

func testLoop(toAccount *big.Int) {
	printConnections()

	from := new(big.Int)
	from = from.Add(n, e)
	time.Sleep(100 * time.Millisecond)

	for i := 1; i <= 250; i++ {
		id := new(big.Int)
		id = big.NewInt(int64(mathrand.Intn(100000000)))
		message := Message{}
		message.MyIP = "127.0.0.1:" + ""
		message.SignedTransaction = SignedTransaction{
			ID:        id.String(),
			From:      from.String(),
			To:        toAccount.String(),
			Amount:    1,
			Signature: getSignature(big.NewInt(int64(1)), from, toAccount, id),
		}
		message.MessageType = "transaction"
		mutexCheck.Lock()
		sendMessage(message)
		mutexCheck.Unlock()
		time.Sleep(100 * time.Millisecond)
	}

	fmt.Println("TEST DONE - WRITE PRINTLEDGER IN EACH SERVER")
}

func (l *Ledger) printLedger() {
	l.lock.Lock()
	defer l.lock.Unlock()
	fmt.Println("\n----- THE CURRENT LEDGER STATE -----")
	for account, money := range l.Accounts {
		fmt.Println("  ...", account, ":", money)
	}
	fmt.Println("------- END OF LEDGER STATE --------\n")
}
