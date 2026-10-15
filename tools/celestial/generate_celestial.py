"""
Best Mates "Daybook" celestial set — original sun and moon artwork, drawn from geometry.

Every glyph is built here from circles, arcs and lines on one 48 x 48 grid:
  * live area 4..44 (the renderer shows the whole view box, so padding is designed, not cropped)
  * one stroke weight (STROKE) with round caps and joins
  * crescents are a disc minus an offset disc, with arcs computed from the exact
    intersections, then optically centred
  * clouds are a flat-based union of three circles; a cloud in front of a sun or moon
    cuts a clean gap out of it (clip path), so the mask stays one colour

The visual language follows the references the user collected (Kustom/KWGT sheets in the
original project's design_refs): bold rounded strokes, substantial crescents with generous
counters, 4-point sparkles, flat-based clouds, horizon lines. No reference path was traced;
the geometry below is original.

Usage:  py -I tools/celestial/generate_celestial.py <assets/solar_icons dir> [preview.html]
"""
import math
import sys
from pathlib import Path

STROKE = 3.5
HALF = STROKE / 2


def f(v):
    s = f"{v:.2f}".rstrip("0").rstrip(".")
    return "0" if s in ("-0", "") else s


# ----------------------------------------------------------------------------- primitives

def circle(cx, cy, r, stroke=False, width=STROKE, opacity=None, extra=""):
    op = f' opacity="{f(opacity)}"' if opacity is not None else ""
    if stroke:
        return f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(r)}" fill="none" stroke="#000" stroke-width="{f(width)}"{op}{extra}/>'
    return f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(r)}" fill="#000"{op}{extra}/>'


def line(x1, y1, x2, y2, width=STROKE, opacity=None):
    op = f' opacity="{f(opacity)}"' if opacity is not None else ""
    return (f'<line x1="{f(x1)}" y1="{f(y1)}" x2="{f(x2)}" y2="{f(y2)}" stroke="#000" '
            f'stroke-width="{f(width)}" stroke-linecap="round"{op}/>')


def path(d, fill=True, width=STROKE, opacity=None, soften=0.0, evenodd=False):
    op = f' opacity="{f(opacity)}"' if opacity is not None else ""
    rule = ' fill-rule="evenodd"' if evenodd else ""
    if fill:
        # A hairline stroke in the same colour softens sharp tips (crescent horns, star points).
        st = (f' stroke="#000" stroke-width="{f(soften)}" stroke-linejoin="round"' if soften else "")
        return f'<path d="{d}" fill="#000"{rule}{st}{op}/>'
    return (f'<path d="{d}" fill="none" stroke="#000" stroke-width="{f(width)}" '
            f'stroke-linecap="round" stroke-linejoin="round"{op}/>')


def polar(cx, cy, r, deg):
    a = math.radians(deg)
    return cx + r * math.cos(a), cy + r * math.sin(a)


def rays(cx, cy, r1, r2, angles, width=STROKE, opacity=None):
    out = []
    for a in angles:
        x1, y1 = polar(cx, cy, r1, a)
        x2, y2 = polar(cx, cy, r2, a)
        out.append(line(x1, y1, x2, y2, width, opacity))
    return out


EIGHT = [i * 45 for i in range(8)]
UPPER = [180, 225, 270, 315, 360]


def dot_rays(cx, cy, r, dot, angles=EIGHT):
    return [circle(*polar(cx, cy, r, a), dot) for a in angles]


def arc_flags(c, r, p0, p1, via):
    """SVG large-arc and sweep flags for the arc of circle (c, r) from p0 to p1 through via."""
    def ang(p):
        return math.atan2(p[1] - c[1], p[0] - c[0]) % (2 * math.pi)
    a0, a1, av = ang(p0), ang(p1), ang(via)
    span_cw = (a1 - a0) % (2 * math.pi)   # increasing angle = clockwise on screen
    via_cw = (av - a0) % (2 * math.pi)
    sweep = 1 if via_cw < span_cw else 0
    span = span_cw if sweep else (2 * math.pi - span_cw)
    return (1 if span > math.pi else 0), sweep


def arc_to(c, r, p0, p1, via):
    large, sweep = arc_flags(c, r, p0, p1, via)
    return f"A{f(r)} {f(r)} 0 {large} {sweep} {f(p1[0])} {f(p1[1])}"


def intersections(c1, r1, c2, r2):
    dx, dy = c2[0] - c1[0], c2[1] - c1[1]
    d = math.hypot(dx, dy)
    a = (d * d + r1 * r1 - r2 * r2) / (2 * d)
    h = math.sqrt(max(r1 * r1 - a * a, 0))
    mx, my = c1[0] + a * dx / d, c1[1] + a * dy / d
    return (mx + h * dy / d, my - h * dx / d), (mx - h * dy / d, my + h * dx / d)


# ----------------------------------------------------------------------------- crescent

def crescent_geometry(R, thickness, direction_deg=-38):
    """Outer disc radius R; the cut disc leaves [thickness] at the back of the crescent."""
    r = R * 0.94
    d = thickness - R + r            # back thickness = R - (r - d)
    ox, oy = polar(0, 0, d, direction_deg)
    return r, (ox, oy)


def crescent_d(cx, cy, R, thickness, direction_deg=-38):
    r, (ox, oy) = crescent_geometry(R, thickness, direction_deg)
    c1, c2 = (cx, cy), (cx + ox, cy + oy)
    p, q = intersections(c1, R, c2, r)
    d = math.hypot(ox, oy)
    far = (cx - R * ox / d, cy - R * oy / d)          # back of the crescent
    near = (c2[0] - r * ox / d, c2[1] - r * oy / d)    # inner edge, nearest the back
    return (f"M{f(p[0])} {f(p[1])} " + arc_to(c1, R, p, q, far) + " " + arc_to(c2, r, q, p, near) + " Z")


def crescent_bbox(cx, cy, R, thickness, direction_deg=-38):
    r, (ox, oy) = crescent_geometry(R, thickness, direction_deg)
    pts = []
    for i in range(720):
        a = 2 * math.pi * i / 720
        x, y = cx + R * math.cos(a), cy + R * math.sin(a)
        if math.hypot(x - cx - ox, y - cy - oy) >= r:
            pts.append((x, y))
        x2, y2 = cx + ox + r * math.cos(a), cy + oy + r * math.sin(a)
        if math.hypot(x2 - cx, y2 - cy) <= R:
            pts.append((x2, y2))
    xs, ys = [p[0] for p in pts], [p[1] for p in pts]
    return min(xs), min(ys), max(xs), max(ys)


def centred_crescent(R, thickness, box=(4, 4, 44, 44), direction_deg=-38):
    """Centre of the outer disc that puts the crescent's bounds in the middle of [box]."""
    x0, y0, x1, y1 = crescent_bbox(0, 0, R, thickness, direction_deg)
    bx = (box[0] + box[2]) / 2 - (x0 + x1) / 2
    by = (box[1] + box[3]) / 2 - (y0 + y1) / 2
    return bx, by


# ----------------------------------------------------------------------------- cloud

class Cloud:
    """Flat-based cloud from three circles: left and right end lobes sit on the base line."""

    def __init__(self, x0, x1, base, height):
        w = x1 - x0
        self.base = base
        rl, rr = height * 0.36, height * 0.33
        rm = height * 0.52
        self.l = (x0 + rl, base - rl, rl)
        self.r = (x1 - rr, base - rr, rr)
        self.m = (x0 + w * 0.47, base - height + rm, rm)

    def d(self, grow=0.0):
        (lx, ly, lr), (mx, my, mr), (rx, ry, rr) = self.l, self.m, self.r
        lr, mr, rr = lr + grow, mr + grow, rr + grow
        base = self.base + grow
        lm = min(intersections((lx, ly), lr, (mx, my), mr), key=lambda p: p[1])
        mrp = min(intersections((mx, my), mr, (rx, ry), rr), key=lambda p: p[1])
        start, end = (lx, base), (rx, base)
        return ("M" + f"{f(start[0])} {f(start[1])} "
                + arc_to((lx, ly), lr, start, lm, (lx - lr, ly)) + " "
                + arc_to((mx, my), mr, lm, mrp, (mx, my - mr)) + " "
                + arc_to((rx, ry), rr, mrp, end, (rx + rr, ry)) + " Z")


def knockout(cloud, gap, body):
    """Draws [body] with a clean gap of [gap] around the cloud that sits in front of it."""
    clip = f'<clipPath id="k"><path clip-rule="evenodd" d="M-8 -8 H56 V56 H-8 Z {cloud.d(gap)}"/></clipPath>'
    return [clip, '<g clip-path="url(#k)">'] + body + ["</g>"]


# ----------------------------------------------------------------------------- ornaments

def sparkle(cx, cy, a, k=0.17):
    """Four-point star with concave sides."""
    t = a * k
    return path(f"M{f(cx)} {f(cy - a)} Q{f(cx + t)} {f(cy - t)} {f(cx + a)} {f(cy)} "
                f"Q{f(cx + t)} {f(cy + t)} {f(cx)} {f(cy + a)} Q{f(cx - t)} {f(cy + t)} {f(cx - a)} {f(cy)} "
                f"Q{f(cx - t)} {f(cy - t)} {f(cx)} {f(cy - a)} Z", soften=0.9)


def plus(cx, cy, a, width=2.8):
    return [line(cx - a, cy, cx + a, cy, width), line(cx, cy - a, cx, cy + a, width)]


def star5(cx, cy, a, inner=0.46):
    pts = []
    for i in range(10):
        r = a if i % 2 == 0 else a * inner
        pts.append(polar(cx, cy, r, -90 + i * 36))
    d = "M" + " L".join(f"{f(x)} {f(y)}" for x, y in pts) + " Z"
    return path(d, soften=1.6)


def zee(x, y, s, width):
    return path(f"M{f(x)} {f(y)} H{f(x + s)} L{f(x)} {f(y + s)} H{f(x + s)}", fill=False, width=width)


def horizon(lines):
    return [line(x1, y, x2, y) for (x1, x2, y) in lines]


def wave(x0, y, period, count, amp):
    d = f"M{f(x0)} {f(y)} q{f(period / 4)} {f(-amp)} {f(period / 2)} 0"
    d += f" t{f(period / 2)} 0" * (count * 2 - 1)
    return path(d, fill=False)


def half_disc(cx, cy, r):
    return path(f"M{f(cx - r)} {f(cy)} A{f(r)} {f(r)} 0 0 1 {f(cx + r)} {f(cy)} Z")


def half_ring(cx, cy, r):
    return path(f"M{f(cx - r)} {f(cy)} A{f(r)} {f(r)} 0 0 1 {f(cx + r)} {f(cy)}", fill=False)


# ----------------------------------------------------------------------------- the set

def suns():
    s = {}
    c = 24
    s["disc"] = [circle(c, c, 8.6)] + rays(c, c, 13.2, 18.4, EIGHT)
    s["ring"] = [circle(c, c, 7.4, stroke=True)] + rays(c, c, 13.2, 18.4, EIGHT)
    s["dots"] = [circle(c, c, 9.4)] + dot_rays(c, c, 16.6, 2.5)
    s["ring_dots"] = [circle(c, c, 8.2, stroke=True)] + dot_rays(c, c, 16.4, 2.3)
    bloom = [circle(c, c, 8.2)]
    for a in EIGHT:
        tip = polar(c, c, 19.4, a)
        l = polar(c, c, 12.2, a - 13)
        r = polar(c, c, 12.2, a + 13)
        bloom.append(path(f"M{f(l[0])} {f(l[1])} L{f(tip[0])} {f(tip[1])} L{f(r[0])} {f(r[1])} Z", soften=2.2))
    s["bloom"] = bloom
    circumference = 2 * math.pi * 15.6
    seg = circumference / 16
    s["halo"] = [circle(c, c, 9.2),
                 f'<circle cx="24" cy="24" r="15.6" fill="none" stroke="#000" stroke-width="3" '
                 f'stroke-linecap="round" stroke-dasharray="{f(seg * .28)} {f(seg * .72)}"/>']

    s["duo"] = [circle(c, c, 17.2, opacity=0.28), circle(c, c, 12.4, opacity=0.5), circle(c, c, 8.4)]
    s["glint"] = [circle(20.5, 27.5, 10.5), sparkle(36.5, 11.5, 6.6), sparkle(40.2, 25.4, 3.4)]
    s["waves"] = ([circle(24, 18.5, 7.2)] + rays(24, 18.5, 11.2, 14.6, UPPER)
                  + [wave(6.5, 32.5, 11.66, 3, 3.2), wave(12.4, 40.2, 7.8, 3, 2.6)])
    s["horizon"] = ([half_disc(24, 28, 10.2)] + rays(24, 28, 14.4, 18.8, [225, 270, 315])
                    + horizon([(5.5, 42.5, 32.6), (11.5, 36.5, 38.2), (17.5, 30.5, 43.8)]))
    s["haze"] = ([half_disc(24, 25.5, 11)] + rays(24, 25.5, 15.2, 19.4, [225, 270, 315])
                 + horizon([(5.5, 42.5, 30.6), (8.5, 19.5, 36.6), (25.5, 39.5, 36.6), (15.5, 32.5, 42.6)]))
    rise = [half_ring(24, 34, 8.6), line(5.5, 34, 42.5, 34), line(14.5, 40.6, 33.5, 40.6)] + rays(24, 34, 13.4, 16.8, [205, 335])
    s["rise"] = rise + [line(24, 20.5, 24, 6.4), path("M18.6 11.8 L24 6.4 L29.4 11.8", fill=False)]
    s["set"] = rise + [line(24, 6.4, 24, 20.5), path("M18.6 15.1 L24 20.5 L29.4 15.1", fill=False)]
    cl = Cloud(5, 38.5, 41, 21.5)
    sun_body = [circle(30.5, 17.5, 7.4)] + rays(30.5, 17.5, 11.6, 15.4, [180, 225, 270, 315, 0, 45])
    s["cloud"] = knockout(cl, 3.2, sun_body) + [path(cl.d())]
    sun_line = [circle(30.5, 17.5, 6.6, stroke=True)] + rays(30.5, 17.5, 11.6, 15.4, [180, 225, 270, 315, 0, 45])
    s["cloud_line"] = knockout(cl, 2.0, sun_line) + [path(cl.d(-HALF), fill=False)]
    return s


def moons():
    m = {}
    R = 17.6
    cx, cy = centred_crescent(R, 13.2)
    m["crescent"] = [path(crescent_d(cx, cy, R, 13.2), soften=1.0)]
    cx, cy = centred_crescent(R - 1.4, 11.0, box=(4 + HALF, 4 + HALF, 44 - HALF, 44 - HALF))
    m["crescent_line"] = [path(crescent_d(cx, cy, R - 1.4, 11.0), fill=False)]
    cx, cy = centred_crescent(R, 7.6)
    m["thin"] = [path(crescent_d(cx, cy, R, 7.6), soften=1.0)]
    # Crescent low and left, sparkles in its opening.
    m["stars"] = [path(crescent_d(21, 27.5, 15.4, 11.6), soften=1.0), sparkle(36.4, 11.6, 6.4), sparkle(40.4, 24.6, 3.3)]
    m["stars_line"] = [path(crescent_d(21.2, 27.6, 14.2, 9.4), fill=False), sparkle(36.4, 11.6, 6.4), sparkle(40.4, 24.6, 3.3)]
    m["plus"] = [path(crescent_d(21.2, 27.6, 14.2, 9.4), fill=False)] + plus(36.6, 11.2, 4.6) + plus(41.2, 23.2, 2.6, width=2.4)
    m["star5"] = [path(crescent_d(21, 25, 16.6, 11.0, -20), soften=1.0), star5(36.2, 21.2, 6.6)]
    cl = Cloud(5, 38.5, 41, 21.5)
    m["cloud"] = knockout(cl, 3.2, [path(crescent_d(30.6, 16.4, 12.2, 9.4, 200), soften=1.0)]) + [path(cl.d())]
    m["cloud_line"] = knockout(cl, 2.0, [path(crescent_d(30.6, 16.4, 11.2, 7.4, 200), fill=False)]) + [path(cl.d(-HALF), fill=False)]
    m["horizon"] = ([path(crescent_d(22.4, 18.6, 12.6, 9.6), soften=1.0), sparkle(37.4, 10.6, 4.4)]
                    + horizon([(5.5, 42.5, 34.6), (11.5, 36.5, 40.4)]))
    m["sleep"] = [path(crescent_d(21, 27.6, 15.4, 11.6), soften=1.0), zee(28.6, 6.8, 8.4, 3.1), zee(38.4, 17.6, 5.4, 2.6)]
    m["full"] = [path("M24 6.4 A17.6 17.6 0 1 1 23.99 6.4 Z "
                      "M18.8 15.6 m-3.6 0 a3.6 3.6 0 1 0 7.2 0 a3.6 3.6 0 1 0 -7.2 0 "
                      "M30.6 26.8 m-4.8 0 a4.8 4.8 0 1 0 9.6 0 a4.8 4.8 0 1 0 -9.6 0 "
                      "M17.6 31.4 m-2.4 0 a2.4 2.4 0 1 0 4.8 0 a2.4 2.4 0 1 0 -4.8 0 Z", evenodd=True)]
    m["ring"] = [circle(24, 24, 17.4, stroke=True, width=3), path(crescent_d(22.4, 24, 11.6, 8.2, 0), soften=1.0)]
    m["duo"] = [circle(24, 24, 17.6, opacity=0.3), path(crescent_d(24, 24, 17.6, 12.6, -38), soften=1.0)]
    return m


SUN_ORDER = ["disc", "ring", "dots", "ring_dots", "bloom", "halo", "duo", "glint", "waves",
             "horizon", "haze", "rise", "set", "cloud", "cloud_line"]
MOON_ORDER = ["crescent", "crescent_line", "thin", "stars", "stars_line", "plus", "star5",
              "cloud", "cloud_line", "horizon", "sleep", "full", "ring", "duo"]


def svg(elements):
    body = "\n  ".join(elements)
    return ('<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48" width="48" height="48">\n'
            f"  {body}\n</svg>\n")


def main():
    out = Path(sys.argv[1])
    out.mkdir(parents=True, exist_ok=True)
    written = []
    for kind, table, order in (("sun", suns(), SUN_ORDER), ("moon", moons(), MOON_ORDER)):
        for key in order:
            name = f"db_{kind}_{key}.svg"
            (out / name).write_text(svg(table[key]), encoding="utf-8")
            written.append(name)
    if len(sys.argv) > 2:
        cells = []
        for name in written:
            src = (out / name).read_text(encoding="utf-8").replace('"#000"', '"currentColor"')
            cells.append(f'<figure class="{name.split("_")[1]}"><div class="big">{src}</div><div class="row"><span class="s18">{src}</span>'
                         f'<span class="s34">{src}</span></div><figcaption>{name[3:-4]}</figcaption></figure>')
        html = ("<!doctype html><meta charset=utf-8><title>Daybook celestial</title><style>"
                "body{margin:12px;background:#1f1c2a;color:#efe7da;font:12px system-ui}"
                "main{display:grid;grid-template-columns:repeat(8,92px);gap:6px}"
                "figure{margin:0;background:#2b2740;border-radius:12px;padding:6px 4px;text-align:center;color:#ffd58a}figure.moon{color:#e4e2ff}"
                ".big svg{width:52px;height:52px;outline:1px dashed #ffffff22}figcaption{font-size:10px;color:#cfc8e8}"
                ".row{display:flex;gap:8px;justify-content:center;align-items:center;margin-top:4px}"
                ".s18 svg{width:18px;height:18px}.s34 svg{width:28px;height:28px}"
                ""
                "</style><main>" + "".join(cells) + "</main>")
        Path(sys.argv[2]).write_text(html, encoding="utf-8")
    print(f"wrote {len(written)} glyphs")


if __name__ == "__main__":
    main()
