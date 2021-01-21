import java.util.Random;

enum DistributionType {
    UNIFORM, GAUSSIAN, EXPONENTIAL, SPECIAL, SPECIAL2, SPECIAL3
}

public class PointGenerator {
    public static Point2D[] generate2D(final DistributionType type, final int n) {
        final Point2D[] result = new Point2D[n];
        final Random random = new Random();

        switch (type) {
            case UNIFORM: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point2D(random.nextDouble(), random.nextDouble());
                }
                break;
            }

            case GAUSSIAN: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point2D(random.nextGaussian(), random.nextGaussian());
                }
                break;
            }

            case EXPONENTIAL: {
                final Random angleRandom = new Random();
                for (int i = 0; i < n; i++) {
                    double distanceFromCenter = Math.log(1 - random.nextDouble()) / (-2);
                    // NOTE: The fractional part stays within (0,1) range and is still exponential.
                    distanceFromCenter -= Math.floor(distanceFromCenter);
                    final double angle = angleRandom.nextDouble() * 2 * Math.PI;
                    result[i] = new Point2D(Math.cos(angle) * distanceFromCenter, Math.sin(angle) * distanceFromCenter);
                }
                throw new UnsupportedOperationException("Exponential point generation has been deprecated!");
            }

            case SPECIAL: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point2D(random.nextDouble(), random.nextGaussian());
                }
                break;
            }

            case SPECIAL2: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point2D(random.nextGaussian(), random.nextDouble());
                }
                break;
            }

            case SPECIAL3: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point2D(random.nextGaussian()/n, random.nextGaussian());
                }
                break;
            }
        }

        return result;
    }

    public static Point3D[] generate3D(final DistributionType type, final int n)
    {
        final Point3D[] result = new Point3D[n];
        final Random random = new Random();

        switch (type) {
            case UNIFORM: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point3D(random.nextDouble(), random.nextDouble(), random.nextDouble());
                }
                break;
            }

            case GAUSSIAN: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point3D(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                }
                break;
            }

            case EXPONENTIAL: {
                throw new UnsupportedOperationException("Exponential point generation has been deprecated!");
            }

            case SPECIAL: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point3D(random.nextGaussian()/20, random.nextGaussian(), random.nextGaussian());
                }
                break;
            }

            case SPECIAL2: {
                throw new UnsupportedOperationException("Special point generation is only for 2D!");
            }
            case SPECIAL3: {
                for (int i = 0; i < n; i++) {
                    result[i] = new Point3D(random.nextGaussian()/20, random.nextGaussian(), random.nextGaussian());
                }
                break;
            }
        }

        return result;
    }
}
