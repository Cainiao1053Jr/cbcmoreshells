package com.cainiao1053.cbcmoreshells.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CBCMSBallisticUtils {
    private static final double EULER = Math.E;
    private static final double MIN_Z = -1.0 / EULER;
    private static final int    MAX_ITERS = 40;
    private static final double TOL = 1e-14;

    public static double solveW(double dy, double theta, double g, double v, double cd) {
        double gS = 400.0 * g;
        double cdT = (1.0 - cd) / 0.05;
        double c = Math.cos(theta);
        if (Math.abs(c) < 1e-12) return Double.NaN;

        double denom = v / cdT;                // v / cdT
        double K1 = 1.0 / (c * denom);         // = cdT / (v c)
        double K0 = 1.0 + 5.0 / denom;         // = 1 + 5 cdT / v
        double a  = gS / (cdT * v * c) + Math.tan(theta);
        double b  = gS / (cdT * cdT);
        double d  = -(gS / cdT) * 5.0 / v + 2.0 - dy;

        if (Math.abs(a) < 1e-14) {
            double E = Math.exp(-d / b);
            double w = (K0 - E) / K1;
            return validateW(w, K0, K1) ? w : Double.NaN;
        }

        double A = K0 / K1;
        double C = -a / b;
        double E = Math.exp(-d / b);
        double B = -E / K1;

        double z = -C * B * Math.exp(C * A);

        double W0 = lambertW0(z);
        double w  = A - (1.0 / C) * W0;
        if (validateW(w, K0, K1)) return w;

        if (z >= -1.0 / EULER && z < 0.0) {
            double Wm1 = lambertWm1(z);
            double w2  = A - (1.0 / C) * Wm1;
            if (validateW(w2, K0, K1)) return w2;
        }

        return Double.NaN;
    }

    private static boolean validateW(double w, double K0, double K1) {
        if (!Double.isFinite(w)) return false;
        if (w < 0.0) return false;
        double inner = K0 - K1 * w;
        return inner > 0.0 && Double.isFinite(inner);
    }

    public static double lambertW0(double z) {
        if (z < MIN_Z) return Double.NaN;
        if (z == 0.0) return 0.0;
        double w = initialGuessW0(z);
        return halleyIter(z, w);
    }

    public static double lambertWm1(double z) {
        if (!(z >= MIN_Z && z < 0.0)) return Double.NaN;
        double w = initialGuessWm1(z);
        return halleyIter(z, w);
    }

    private static double halleyIter(double z, double w0) {
        double w = w0;
        for (int i = 0; i < MAX_ITERS; i++) {
            double ew = Math.exp(w);
            double wew = w * ew;
            double f = wew - z;
            double wp1 = w + 1.0;
            double denom = ew * wp1 - 0.5 * (wp1 + 1.0) * f / wp1;
            if (denom == 0.0) denom = ew * wp1;
            double dw = f / denom;
            double wNext = w - dw;

            if (!Double.isFinite(wNext)) {
                double f1 = ew * wp1;
                double f2 = ew * (w + 2.0);
                double halley = (2.0 * f * f1) / (2.0 * f1 * f1 - f * f2);
                wNext = w - halley;
            }

            if (Math.abs(wNext - w) <= TOL * Math.max(1.0, Math.abs(wNext))) return wNext;
            w = wNext;
        }
        return w;
    }

    private static double initialGuessW0(double z) {
        if (z >= 8.0) {
            return Math.log(z) - Math.log(Math.log(z)); // 渐近
        } else if (z > -0.3) {
            return z;
        } else {
            double q = z + MIN_Z * -1.0;
            q = z + 1.0 / EULER;
            double t = Math.sqrt(Math.max(0.0, 2.0 * EULER * q));
            return -1.0 + t - t * t / 3.0;
        }
    }


    private static double initialGuessWm1(double z) {
        double q = z + 1.0 / EULER;
        double t = Math.sqrt(Math.max(0.0, 2.0 * EULER * q));
        double w = -1.0 - t - t * t / 3.0;
        if (z > -1e-3) {
            w = Math.log(-z);
        }
        return w;
    }

    public static double cannonFunction(double theta, double w, double g, double v, double cd) { //v = 40*n
        g = 400*g;
        double cdT = (1-cd)/0.05;
        double c = Math.cos(theta);
        if (Math.abs(c) < 1e-12) {
            return Math.copySign(0, Math.tan(theta));
        }

        double term1 = ((g / cdT) / (v)) / c + Math.tan(theta);
        term1 *= w;

        double denom = (1.0 / cdT) * v;
        double inner = 1.0 - (w / c - 5) / denom; // k = 5

        if (inner <= 0.0) {
            return Math.copySign(0, -1.0);
        }

        double term2 = (g / (cdT * cdT)) * Math.log(inner);
        double term3 = -(g / cdT) * 5 / (v);
        double term4 = 2.0;

        return term1 + term2 + term3 + term4;
    }

    public static double vecToPitch(Vec3 v) {
        if (v == null) return 0;

        Vec3 n = v.normalize();
        if (Double.isNaN(n.x) || Double.isNaN(n.y) || Double.isNaN(n.z)) {
            return 0;
        }

        // Minecraft约定：
        // yaw: 围绕Y轴，0朝南(+Z)，90朝西(-X)，-90朝东(+X)
        // pitch: 抬头为负，低头为正
        //float yaw = (float) (Mth.atan2(n.z, n.x) * Mth.RAD_TO_DEG) - 90f;

        // 水平长度
        double xz = Math.sqrt(n.x * n.x + n.z * n.z);
        double pitch = (Mth.atan2(n.y, xz));

        // 按MC习惯：向上看为负
        //pitch = -pitch;

        return clampPitch(pitch);
    }

    private static float wrapDeg(float deg) {
        return Mth.wrapDegrees(deg);
    }

    private static double clampPitch(double pitch) {
        return Mth.clamp(pitch, -Math.PI/2, Math.PI/2);
    }

    // =============================================================================================
    // Dual cannon ballistics
    //
    // Dual cannon shells are integrated by shaolib's MunitionMotionModels.configured(...), wired up
    // in CBCMSDualCannonProjectiles. Every tick it does, with dt = 1:
    //     a  = (0, gravity, 0) - drag * v
    //     p += v * dt + 0.5 * a * dt^2
    //     v += a * dt
    // Drag is applied per component, so the axes decouple completely and the recurrence
    // v[n+1] = (1 - drag) * v[n] + g has an exact solution. Everything below is that solution, so
    // these helpers are tick-exact rather than a continuous-time approximation.
    //
    // Units, taken straight off the shell's properties:
    //   v0      DualCannonLaunchProperties.initialVelocity, blocks/tick (tooltips show it * 20)
    //   drag    BallisticsProperties.drag, the fraction of speed lost per tick (default 0.01)
    //   gravity BallisticsProperties.gravity, blocks/tick^2 and negative (default -0.05)
    // Quadratic drag is NOT covered; BallisticsProperties.quadraticDrag is false for every dual
    // cannon shell, and the closed form does not survive it.
    //
    // Distances and heights are measured from the muzzle, so a level shot has dy = 0. cannonFunction
    // above cannot be used for this: it bakes in the CBC big cannon muzzle geometry (a 5 block
    // forward offset and a +2 height offset) and takes its muzzle velocity in blocks/second.
    // =============================================================================================

    private static final double MIN_DRAG = 1.0e-9;
    private static final double MIN_COS = 1.0e-9;
    private static final int SEARCH_ITERS = 60;
    private static final int MAX_REPLAY_TICKS = 6000;

    /** A solved firing solution against a target on the muzzle's own level. */
    public record DualCannonShot(double angle, double range, double flightTicks, double impactSpeed) {}

    /** One row of a trajectory table: where the shell is, when it gets there, and how fast. */
    public record DualCannonSample(double distance, double height, double ticks, double speed) {}

    /** Blocks of horizontal travel bought by burning the whole speed budget: (1 - drag/2) / drag. */
    private static double dragScale(double drag) {
        return (1.0 - 0.5 * drag) / drag;
    }

    /**
     * Horizontal distance the shell would reach if it never came down. Drag eats a fixed fraction of
     * the remaining horizontal speed each tick, so this is finite and no shot can pass it.
     */
    public static double dualCannonReach(double theta, double v0, double drag) {
        double vx = v0 * Math.cos(theta);
        if (!(vx > 0.0)) return 0.0;
        if (drag <= MIN_DRAG) return Double.POSITIVE_INFINITY;
        return dragScale(drag) * vx;
    }

    /** Fraction of {@link #dualCannonReach} spent at horizontal distance {@code x}, in [0, 1). */
    private static double spentFraction(double x, double theta, double v0, double drag) {
        double reach = dualCannonReach(theta, v0, drag);
        if (!(reach > 0.0)) return Double.NaN;
        double u = x / reach;
        return u >= 0.0 && u < 1.0 ? u : Double.NaN;
    }

    /** Height above the muzzle at horizontal distance {@code x}, or NaN if the shell cannot get there. */
    public static double dualCannonHeightAt(double x, double theta, double v0, double drag, double gravity) {
        if (x <= 0.0) return 0.0;
        double cos = Math.cos(theta);
        if (cos < MIN_COS || !(v0 > 0.0)) return Double.NaN;

        double vx = v0 * cos;
        double vy = v0 * Math.sin(theta);
        if (drag <= MIN_DRAG) {
            double ticks = x / vx;
            return vy * ticks + 0.5 * gravity * ticks * ticks;
        }

        double u = spentFraction(x, theta, v0, drag);
        if (Double.isNaN(u)) return Double.NaN;
        double terminal = gravity / drag; // the speed vertical drag settles at, negative
        return dragScale(drag) * (vy - terminal) * u + terminal * Math.log1p(-u) / Math.log1p(-drag);
    }

    /** Flight time in ticks to horizontal distance {@code x}, or NaN if the shell cannot get there. */
    public static double dualCannonTicksAt(double x, double theta, double v0, double drag) {
        if (x <= 0.0) return 0.0;
        double cos = Math.cos(theta);
        if (cos < MIN_COS || !(v0 > 0.0)) return Double.NaN;
        if (drag <= MIN_DRAG) return x / (v0 * cos);

        double u = spentFraction(x, theta, v0, drag);
        return Double.isNaN(u) ? Double.NaN : Math.log1p(-u) / Math.log1p(-drag);
    }

    /** Horizontal distance covered after {@code ticks} of flight. */
    public static double dualCannonDistanceAtTicks(double ticks, double theta, double v0, double drag) {
        double vx = v0 * Math.cos(theta);
        if (!(vx > 0.0) || ticks <= 0.0) return 0.0;
        if (drag <= MIN_DRAG) return vx * ticks;
        return dragScale(drag) * vx * (1.0 - Math.pow(1.0 - drag, ticks));
    }

    /** Velocity in the firing plane at {@code ticks} after launch: x is downrange, y is up, z unused. */
    public static Vec3 dualCannonVelocityAt(double ticks, double theta, double v0, double drag, double gravity) {
        double vx = v0 * Math.cos(theta);
        double vy = v0 * Math.sin(theta);
        if (ticks <= 0.0) return new Vec3(vx, vy, 0.0);
        if (drag <= MIN_DRAG) return new Vec3(vx, vy + gravity * ticks, 0.0);

        double decay = Math.pow(1.0 - drag, ticks);
        double terminal = gravity / drag;
        return new Vec3(vx * decay, terminal + (vy - terminal) * decay, 0.0);
    }

    /** Remaining speed in blocks/tick at horizontal distance {@code x}. Feeds the penetration model. */
    public static double dualCannonSpeedAt(double x, double theta, double v0, double drag, double gravity) {
        double ticks = dualCannonTicksAt(x, theta, v0, drag);
        if (Double.isNaN(ticks)) return Double.NaN;
        return dualCannonVelocityAt(ticks, theta, v0, drag, gravity).length();
    }

    /**
     * Horizontal distance at which the shell comes back down to muzzle level, i.e. the range of this
     * elevation for a zero height difference.
     */
    public static double dualCannonFlatRange(double theta, double v0, double drag, double gravity) {
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        if (cos < MIN_COS || !(v0 > 0.0) || !(gravity < 0.0)) return Double.NaN;
        if (sin < 0.0) return 0.0;

        double vx = v0 * cos;
        double vy = v0 * sin;
        if (drag <= MIN_DRAG) return -2.0 * vx * vy / gravity;

        // Height reduces to rise * u + fall * ln(1 - u) with u the spent fraction of the reach; the
        // non-zero root of that is where the shell lands.
        double terminal = gravity / drag;                 // < 0
        double rise = dragScale(drag) * (vy - terminal);  // > 0
        double fall = terminal / Math.log1p(-drag);       // > 0
        double ratio = rise / fall;
        if (!(ratio > 1.0)) return 0.0;

        double u = flatRangeFraction(ratio);
        return Double.isNaN(u) ? Double.NaN : dragScale(drag) * vx * u;
    }

    /**
     * Solves {@code ratio * u + ln(1 - u) == 0} for the root in (0, 1). Lambert W gives it in closed
     * form, but the argument sits near the -1/e branch point for near-flat shots, so the result is
     * polished with a bracketed Newton.
     */
    private static double flatRangeFraction(double ratio) {
        double u = 1.0 + lambertW0(-ratio * Math.exp(-ratio)) / ratio;
        if (!(u > 0.0) || !(u < 1.0)) u = 0.5;

        double lo = 0.0;
        double hi = 1.0;
        for (int i = 0; i < MAX_ITERS; i++) {
            double f = ratio * u + Math.log1p(-u);
            if (f > 0.0) lo = u; else hi = u;

            double slope = ratio - 1.0 / (1.0 - u);
            double next = slope != 0.0 ? u - f / slope : Double.NaN;
            if (!(next > lo) || !(next < hi)) next = 0.5 * (lo + hi);
            if (Math.abs(next - u) <= TOL * Math.max(1.0, Math.abs(next))) return next;
            u = next;
        }
        return u > 0.0 && u < 1.0 ? u : Double.NaN;
    }

    /** The full solution for one elevation, or null if that elevation never comes back down. */
    public static DualCannonShot dualCannonShotAt(double theta, double v0, double drag, double gravity) {
        double range = dualCannonFlatRange(theta, v0, drag, gravity);
        if (Double.isNaN(range)) return null;
        return new DualCannonShot(theta, range, dualCannonTicksAt(range, theta, v0, drag),
            dualCannonSpeedAt(range, theta, v0, drag, gravity));
    }

    /**
     * Elevation that reaches furthest, and the shot it produces. Drag pulls the optimum well below
     * the drag-free 45 degrees, so this is searched rather than assumed.
     *
     * <p>This is the purely ballistic ceiling. A shell can still fall short of it by expiring first
     * ({@code DualCannonState.lifetimeTicks}, compare against {@link DualCannonShot#flightTicks})
     * or by running out of its travel budget ({@code RuntimeProperties.maxDistance}, compare
     * against {@link #dualCannonPathLengthAt}).
     */
    public static DualCannonShot dualCannonMaxRange(double v0, double drag, double gravity) {
        if (!(v0 > 0.0) || !(gravity < 0.0)) return null;

        double lo = 0.0;
        double hi = Math.PI / 2.0 - 1.0e-4;
        for (int i = 0; i < SEARCH_ITERS; i++) {
            double third = (hi - lo) / 3.0;
            double a = lo + third;
            double b = hi - third;
            if (rangeOrZero(a, v0, drag, gravity) < rangeOrZero(b, v0, drag, gravity)) lo = a; else hi = b;
        }
        return dualCannonShotAt(0.5 * (lo + hi), v0, drag, gravity);
    }

    public static DualCannonShot dualCannonMaxRangeWithLimit(double v0, double drag, double gravity, double maxRange){
        DualCannonShot initial = dualCannonMaxRange(v0, drag, gravity);
        if(initial != null && initial.range() > maxRange){
            return new CBCMSBallisticUtils.DualCannonShot(initial.angle(), maxRange, initial.flightTicks(), initial.impactSpeed());
        }
        return initial;
    }

    private static double rangeOrZero(double theta, double v0, double drag, double gravity) {
        double range = dualCannonFlatRange(theta, v0, drag, gravity);
        return Double.isNaN(range) ? 0.0 : range;
    }

    /**
     * Elevation that lands the shell {@code range} blocks downrange at muzzle level, with the flight
     * time and remaining speed it arrives with. {@code highArc} picks the lofted solution instead of
     * the flat one. Null when the range is out of reach.
     */
    public static DualCannonShot dualCannonSolveRange(double range, double v0, double drag, double gravity,
                                                      boolean highArc) {
        return solveRange(range, v0, drag, gravity, highArc, dualCannonMaxRange(v0, drag, gravity));
    }

    private static DualCannonShot solveRange(double range, double v0, double drag, double gravity, boolean highArc,
                                             DualCannonShot furthest) {
        if (furthest == null || !(range > 0.0) || range > furthest.range()) return null;

        // Range rises monotonically up to the optimum and falls monotonically past it, so each arc is
        // a plain bisection.
        double lo = highArc ? furthest.angle() : 0.0;
        double hi = highArc ? Math.PI / 2.0 - 1.0e-4 : furthest.angle();
        for (int i = 0; i < SEARCH_ITERS; i++) {
            double mid = 0.5 * (lo + hi);
            boolean climb = highArc == (rangeOrZero(mid, v0, drag, gravity) > range);
            if (climb) lo = mid; else hi = mid;
        }
        return dualCannonShotAt(0.5 * (lo + hi), v0, drag, gravity);
    }

    /**
     * Path length flown to reach horizontal distance {@code x}. This is what
     * {@code DualCannonBehavior} accumulates into {@code DualCannonState.travelled()} and checks
     * against {@code RuntimeProperties.maxDistance}, and it has no closed form, so the integrator is
     * replayed tick by tick.
     */
    public static double dualCannonPathLengthAt(double x, double theta, double v0, double drag, double gravity) {
        double ticks = dualCannonTicksAt(x, theta, v0, drag);
        if (Double.isNaN(ticks) || ticks > MAX_REPLAY_TICKS) return Double.NaN;

        double vx = v0 * Math.cos(theta);
        double vy = v0 * Math.sin(theta);
        double travelled = 0.0;
        int whole = (int) ticks;
        for (int i = 0; i < whole; i++) {
            double ax = -drag * vx;
            double ay = gravity - drag * vy;
            travelled += Math.hypot(vx + 0.5 * ax, vy + 0.5 * ay);
            vx += ax;
            vy += ay;
        }
        double rest = ticks - whole;
        if (rest > 0.0) {
            double ax = -drag * vx;
            double ay = gravity - drag * vy;
            travelled += rest * Math.hypot(vx + 0.5 * ax * rest, vy + 0.5 * ay * rest);
        }
        return travelled;
    }

    /**
     * Samples the arc of one elevation from the muzzle to where it returns to muzzle level, evenly
     * spaced in horizontal distance. Intended for drawing the trajectory in world.
     */
    public static List<DualCannonSample> dualCannonTrajectory(double theta, double v0, double drag, double gravity,
                                                              int samples) {
        List<DualCannonSample> path = new ArrayList<>();
        double range = dualCannonFlatRange(theta, v0, drag, gravity);
        if (Double.isNaN(range) || samples < 2) return path;

        for (int i = 0; i < samples; i++) {
            double x = range * i / (samples - 1.0);
            path.add(new DualCannonSample(x, dualCannonHeightAt(x, theta, v0, drag, gravity),
                dualCannonTicksAt(x, theta, v0, drag), dualCannonSpeedAt(x, theta, v0, drag, gravity)));
        }
        return path;
    }

    /**
     * Range/elevation rows for an in-game firing table: {@code rows} ranges evenly spaced up to the
     * shell's maximum, each with the elevation, flight time and impact speed needed to hit it.
     */
    public static List<DualCannonShot> dualCannonRangeTable(double v0, double drag, double gravity, int rows,
                                                            boolean highArc, double maxRange) {
        List<DualCannonShot> table = new ArrayList<>();
        DualCannonShot furthest = dualCannonMaxRangeWithLimit(v0, drag, gravity, maxRange);
        if (furthest == null || rows < 1) return table;

        for (int i = 1; i <= rows; i++) {
            DualCannonShot shot = solveRange(furthest.range() * i / rows, v0, drag, gravity, highArc, furthest);
            if (shot != null) table.add(shot);
        }
        return table;
    }
}
