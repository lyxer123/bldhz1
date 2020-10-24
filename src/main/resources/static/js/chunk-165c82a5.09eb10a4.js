(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["chunk-165c82a5"], {
    1148: function (t, e, a) {
        "use strict";
        var i = a("a691"), n = a("1d80");
        t.exports = "".repeat || function (t) {
            var e = String(n(this)), a = "", r = i(t);
            if (r < 0 || r == 1 / 0) throw RangeError("Wrong number of repetitions");
            for (; r > 0; (r >>>= 1) && (e += e)) 1 & r && (a += e);
            return a
        }
    }, "408a": function (t, e, a) {
        var i = a("c6b6");
        t.exports = function (t) {
            if ("number" != typeof t && "Number" != i(t)) throw TypeError("Incorrect invocation");
            return +t
        }
    }, 76612: function (t, e, a) {
        "use strict";
        a.d(e, "a", (function () {
            return r
        })), a.d(e, "b", (function () {
            return o
        }));
        var i = a("b775"), n = "block/";

        function r(t) {
            return Object(i["a"])({
                url: n + (t ? "searchDeviceGps.json?chipId=" + t : "searchDeviceGps.json"),
                method: "get"
            })
        }

        function o(t) {
            return Object(i["a"])({url: n + "updateDeviceGps.json", method: "post", data: t})
        }
    }, "7abe": function (t, e, a) {
        "use strict";
        a.r(e);
        var i, n = function () {
                var t = this, e = t.$createElement, a = t._self._c || e;
                return a("div", {staticClass: "home-container"}, [a("panel-group", {attrs: {data: t.block_show_card}}), a("el-amap", {
                    staticClass: "map",
                    attrs: {vid: "amapDemo", zoom: t.zoom, center: t.center}
                }, [t._l(t.markers, (function (t) {
                    return [a("el-amap-marker", {attrs: {position: t.position, title: t.title, content: t.content}})]
                }))], 2)], 1)
            }, r = [], o = (a("b680"), a("ac1f"), a("1276"), function () {
                var t = this, e = t.$createElement, a = t._self._c || e;
                return a("el-row", {staticClass: "panel-group", attrs: {gutter: 40}}, [t._l(t.data, (function (e, i) {
                    return [i < 4 && e.show ? a("el-col", {
                        staticClass: "card-panel-col",
                        attrs: {xs: 12, sm: 12, lg: 6}
                    }, [a("div", {staticClass: "card-panel"}, [a("div", {staticClass: "card-panel-icon-wrapper"}, [a("svg-icon", {
                        attrs: {
                            "icon-class": e.icon,
                            "class-name": "card-panel-icon"
                        }
                    })], 1), a("div", {staticClass: "card-panel-description"}, [a("div", {staticClass: "card-panel-text"}, [t._v(" " + t._s(e.k) + " ")]), a("count-to", {
                        staticClass: "card-panel-num",
                        attrs: {"start-val": e.oldV, "end-val": e.v, duration: 2600}
                    })], 1)])]) : t._e(), i > 3 && e.show ? a("el-col", {
                        staticClass: "card-panel-col",
                        attrs: {xs: 12, sm: 12, lg: 6}
                    }, [a("div", {staticClass: "card-panel"}, [a("div", {staticClass: "card-panel-icon-wrapper"}, [a("svg-icon", {
                        attrs: {
                            "icon-class": e.icon,
                            "class-name": "card-panel-icon"
                        }
                    })], 1), a("div", {staticClass: "card-panel-description"}, [a("div", {staticClass: "card-panel-text"}, [t._v(" " + t._s(e.k) + " ")]), a("div", {staticStyle: {"font-size": ".4rem"}}, [t._v(t._s(e.v))])])])]) : t._e()]
                }))], 2)
            }), s = [], c = a("ec1b"), l = a.n(c), u = {props: ["data"], components: {CountTo: l.a}}, d = u,
            h = (a("d84d"), a("2877")), f = Object(h["a"])(d, o, s, !1, null, "60545593", null), m = f.exports,
            p = a("76612"), w = {
                name: "home", data: function () {
                    return {
                        block_show_card: [{k: "总设备", v: 600, oldV: 0, icon: "device", show: !0}, {
                            k: "北京设备",
                            v: 20,
                            oldV: 0,
                            icon: "beijing",
                            show: !0
                        }, {k: "江苏设备", v: 500, oldV: 0, icon: "jiangsu", show: !0}, {
                            k: "山东设备",
                            v: 80,
                            oldV: 0,
                            icon: "" +
                                "",
                            show: !0
                        }, {k: "区块高度", v: 0, oldV: 0, icon: "blockHeight", show: !0}, {
                            k: "最近出块时间",
                            v: "5s ago",
                            time: 4,
                            icon: "time",
                            show: !0
                        }, {k: "平均出块时间", v: "12.52s", icon: "hourglass", show: !0}, {
                            k: "计算难度",
                            v: "9.45 MH",
                            icon: "diff",
                            show: !0
                        }], zoom: 4, center: [106.533051, 29.466567], markers: []
                    }
                }, components: {PanelGroup: m}, created: function () {
                    var t = this, e = this;
                    setInterval((function () {
                        e.block_show_card[5].v = e.block_show_card[5].time + "s ago", e.block_show_card[5].time += 1
                    }), 1e3), i = new WebSocket("ws://125.64.98.21:3000/primus/?_primuscb=" + (new Date).getTime() + "-1"), i.onopen = function () {
                        i.send(JSON.stringify({emit: ["ready"]}))
                    }, i.onmessage = function (t) {

                        var a = JSON.parse(t.data), n = a.emit;
                        if (n && "init" === n[0]) e.block_show_card[4].v = n[1].nodes[0].stats.block.number, e.block_show_card[5].time = Math.round((new Date).getTime() / 1e3 - n[1].nodes[0].stats.block.timestamp), e.block_show_card[7].v = (n[1].nodes[0].stats.block.difficulty / 1e6).toFixed(2) + "MH"; else if ("charts" === a.action) e.block_show_card[6].v = a.data.avgBlocktime.toFixed(2) + "s";
                        else if ("block" === a.action) {
                            var r = a.data.block.number;
                            r > e.block_show_card[4].v && (e.block_show_card[4].oldV = e.block_show_card[4].v, e.block_show_card[4].v = r, e.block_show_card[5].time = 0, e.block_show_card[7].v = (a.data.block.difficulty / 1e6).toFixed(2) + "MH")
                        } else "client-ping" === a.action && i.send(JSON.stringify({
                            emit: ["client-pong", {
                                serverTime: a.data.serverTime,
                                clientTime: (new Date).getTime()
                            }]
                        }))
                    }, Object(p["a"])().then((function (e) {
                        if (e.success) {
                            t.markers = [];
                            for (var a = e.data.length, i = 0; i < a; i++) {
                                var n = e.data[i], r = n.gps;
                                if (r) {
                                    n.addr;
                                    t.markers.push({position: r.split(","), title: "设备：" + n.chipId + "，地址：" + n.addr})
                                }
                            }
                        }
                    }));
                    var a = this.$store.state.user.user, n = a.authority;
                    if ("TENANT_ADMIN" === n || "SYS_ADMIN" === n) e.zoom = 4; else if ("CUSTOMER_USER" === n) {
                        e.zoom = 8, e.block_show_card[0].show = !1;
                        var r = a.customerId.id;
                        "4ad271a0-bf61-11ea-aab8-4bc49e65094a" === r ? (e.block_show_card[2].show = !1, e.block_show_card[3].show = !1, e.block_show_card[7].show = !1, e.center = [116.403694, 39.913164]) : "4867b150-2b73-11ea-b340-81a9d94af463" === r ? (e.block_show_card[1].show = !1, e.block_show_card[3].show = !1, e.block_show_card[7].show = !1, e.center = [118.803473, 32.076079]) : "e3d7f020-2b71-11ea-b340-81a9d94af463" === r ? (e.block_show_card[1].show = !1, e.block_show_card[2].show = !1, e.block_show_card[7].show = !1, e.center = [117.028017, 36.669299]) : (e.block_show_card[1].show = !1, e.block_show_card[2].show = !1, e.block_show_card[3].show = !1, e.zoom = 4)
                    }
                }
            }, b = w, v = (a("ca14"), Object(h["a"])(b, n, r, !1, null, "2d0ff1f7", null));
        e["default"] = v.exports
    }, "7e88": function (t, e, a) {
    }, b680: function (t, e, a) {
        "use strict";
        var i = a("23e7"), n = a("a691"), r = a("408a"), o = a("1148"), s = a("d039"), c = 1..toFixed, l = Math.floor,
            u = function (t, e, a) {
                return 0 === e ? a : e % 2 === 1 ? u(t, e - 1, a * t) : u(t * t, e / 2, a)
            }, d = function (t) {
                var e = 0, a = t;
                while (a >= 4096) e += 12, a /= 4096;
                while (a >= 2) e += 1, a /= 2;
                return e
            },
            h = c && ("0.000" !== 8e-5.toFixed(3) || "1" !== .9.toFixed(0) || "1.25" !== 1.255.toFixed(2) || "1000000000000000128" !== (0xde0b6b3a7640080).toFixed(0)) || !s((function () {
                c.call({})
            }));
        i({target: "Number", proto: !0, forced: h}, {
            toFixed: function (t) {
                var e, a, i, s, c = r(this), h = n(t), f = [0, 0, 0, 0, 0, 0], m = "", p = "0", w = function (t, e) {
                    var a = -1, i = e;
                    while (++a < 6) i += t * f[a], f[a] = i % 1e7, i = l(i / 1e7)
                }, b = function (t) {
                    var e = 6, a = 0;
                    while (--e >= 0) a += f[e], f[e] = l(a / t), a = a % t * 1e7
                }, v = function () {
                    var t = 6, e = "";
                    while (--t >= 0) if ("" !== e || 0 === t || 0 !== f[t]) {
                        var a = String(f[t]);
                        e = "" === e ? a : e + o.call("0", 7 - a.length) + a
                    }
                    return e
                };
                if (h < 0 || h > 20) throw RangeError("Incorrect fraction digits");
                if (c != c) return "NaN";
                if (c <= -1e21 || c >= 1e21) return String(c);
                if (c < 0 && (m = "-", c = -c), c > 1e-21) if (e = d(c * u(2, 69, 1)) - 69, a = e < 0 ? c * u(2, -e, 1) : c / u(2, e, 1), a *= 4503599627370496, e = 52 - e, e > 0) {
                    w(0, a), i = h;
                    while (i >= 7) w(1e7, 0), i -= 7;
                    w(u(10, i, 1), 0), i = e - 1;
                    while (i >= 23) b(1 << 23), i -= 23;
                    b(1 << i), w(1, 1), b(2), p = v()
                } else w(0, a), w(1 << -e, 0), p = v() + o.call("0", h);
                return h > 0 ? (s = p.length, p = m + (s <= h ? "0." + o.call("0", h - s) + p : p.slice(0, s - h) + "." + p.slice(s - h))) : p = m + p, p
            }
        })
    }, c62cb: function (t, e, a) {
    }, ca14: function (t, e, a) {
        "use strict";
        var i = a("c62cb"), n = a.n(i);
        n.a
    }, d84d: function (t, e, a) {
        "use strict";
        var i = a("7e88"), n = a.n(i);
        n.a
    }, ec1b: function (t, e, a) {
        !function (e, a) {
            t.exports = a()
        }(0, (function () {
            return function (t) {
                function e(i) {
                    if (a[i]) return a[i].exports;
                    var n = a[i] = {i: i, l: !1, exports: {}};
                    return t[i].call(n.exports, n, n.exports, e), n.l = !0, n.exports
                }

                var a = {};
                return e.m = t, e.c = a, e.i = function (t) {
                    return t
                }, e.d = function (t, a, i) {
                    e.o(t, a) || Object.defineProperty(t, a, {configurable: !1, enumerable: !0, get: i})
                }, e.n = function (t) {
                    var a = t && t.__esModule ? function () {
                        return t.default
                    } : function () {
                        return t
                    };
                    return e.d(a, "a", a), a
                }, e.o = function (t, e) {
                    return Object.prototype.hasOwnProperty.call(t, e)
                }, e.p = "/dist/", e(e.s = 2)
            }([function (t, e, a) {
                var i = a(4)(a(1), a(5), null, null);
                t.exports = i.exports
            }, function (t, e, a) {
                "use strict";
                Object.defineProperty(e, "__esModule", {value: !0});
                var i = a(3);
                e.default = {
                    props: {
                        startVal: {type: Number, required: !1, default: 0},
                        endVal: {type: Number, required: !1, default: 2017},
                        duration: {type: Number, required: !1, default: 3e3},
                        autoplay: {type: Boolean, required: !1, default: !0},
                        decimals: {
                            type: Number, required: !1, default: 0, validator: function (t) {
                                return t >= 0
                            }
                        },
                        decimal: {type: String, required: !1, default: "."},
                        separator: {type: String, required: !1, default: ","},
                        prefix: {type: String, required: !1, default: ""},
                        suffix: {type: String, required: !1, default: ""},
                        useEasing: {type: Boolean, required: !1, default: !0},
                        easingFn: {
                            type: Function, default: function (t, e, a, i) {
                                return a * (1 - Math.pow(2, -10 * t / i)) * 1024 / 1023 + e
                            }
                        }
                    }, data: function () {
                        return {
                            localStartVal: this.startVal,
                            displayValue: this.formatNumber(this.startVal),
                            printVal: null,
                            paused: !1,
                            localDuration: this.duration,
                            startTime: null,
                            timestamp: null,
                            remaining: null,
                            rAF: null
                        }
                    }, computed: {
                        countDown: function () {
                            return this.startVal > this.endVal
                        }
                    }, watch: {
                        startVal: function () {
                            this.autoplay && this.start()
                        }, endVal: function () {
                            this.autoplay && this.start()
                        }
                    }, mounted: function () {
                        this.autoplay && this.start(), this.$emit("mountedCallback")
                    }, methods: {
                        start: function () {
                            this.localStartVal = this.startVal, this.startTime = null, this.localDuration = this.duration, this.paused = !1, this.rAF = (0, i.requestAnimationFrame)(this.count)
                        }, pauseResume: function () {
                            this.paused ? (this.resume(), this.paused = !1) : (this.pause(), this.paused = !0)
                        }, pause: function () {
                            (0, i.cancelAnimationFrame)(this.rAF)
                        }, resume: function () {
                            this.startTime = null, this.localDuration = +this.remaining, this.localStartVal = +this.printVal, (0, i.requestAnimationFrame)(this.count)
                        }, reset: function () {
                            this.startTime = null, (0, i.cancelAnimationFrame)(this.rAF), this.displayValue = this.formatNumber(this.startVal)
                        }, count: function (t) {
                            this.startTime || (this.startTime = t), this.timestamp = t;
                            var e = t - this.startTime;
                            this.remaining = this.localDuration - e, this.useEasing ? this.countDown ? this.printVal = this.localStartVal - this.easingFn(e, 0, this.localStartVal - this.endVal, this.localDuration) : this.printVal = this.easingFn(e, this.localStartVal, this.endVal - this.localStartVal, this.localDuration) : this.countDown ? this.printVal = this.localStartVal - (this.localStartVal - this.endVal) * (e / this.localDuration) : this.printVal = this.localStartVal + (this.localStartVal - this.startVal) * (e / this.localDuration), this.countDown ? this.printVal = this.printVal < this.endVal ? this.endVal : this.printVal : this.printVal = this.printVal > this.endVal ? this.endVal : this.printVal, this.displayValue = this.formatNumber(this.printVal), e < this.localDuration ? this.rAF = (0, i.requestAnimationFrame)(this.count) : this.$emit("callback")
                        }, isNumber: function (t) {
                            return !isNaN(parseFloat(t))
                        }, formatNumber: function (t) {
                            t = t.toFixed(this.decimals), t += "";
                            var e = t.split("."), a = e[0], i = e.length > 1 ? this.decimal + e[1] : "",
                                n = /(\d+)(\d{3})/;
                            if (this.separator && !this.isNumber(this.separator)) for (; n.test(a);) a = a.replace(n, "$1" + this.separator + "$2");
                            return this.prefix + a + i + this.suffix
                        }
                    }, destroyed: function () {
                        (0, i.cancelAnimationFrame)(this.rAF)
                    }
                }
            }, function (t, e, a) {
                "use strict";
                Object.defineProperty(e, "__esModule", {value: !0});
                var i = a(0), n = function (t) {
                    return t && t.__esModule ? t : {default: t}
                }(i);
                e.default = n.default, "undefined" != typeof window && window.Vue && window.Vue.component("count-to", n.default)
            }, function (t, e, a) {
                "use strict";
                Object.defineProperty(e, "__esModule", {value: !0});
                var i = 0, n = "webkit moz ms o".split(" "), r = void 0, o = void 0;
                if ("undefined" == typeof window) e.requestAnimationFrame = r = function () {
                }, e.cancelAnimationFrame = o = function () {
                }; else {
                    e.requestAnimationFrame = r = window.requestAnimationFrame, e.cancelAnimationFrame = o = window.cancelAnimationFrame;
                    for (var s = void 0, c = 0; c < n.length && (!r || !o); c++) s = n[c], e.requestAnimationFrame = r = r || window[s + "RequestAnimationFrame"], e.cancelAnimationFrame = o = o || window[s + "CancelAnimationFrame"] || window[s + "CancelRequestAnimationFrame"];
                    r && o || (e.requestAnimationFrame = r = function (t) {
                        var e = (new Date).getTime(), a = Math.max(0, 16 - (e - i)),
                            n = window.setTimeout((function () {
                                t(e + a)
                            }), a);
                        return i = e + a, n
                    }, e.cancelAnimationFrame = o = function (t) {
                        window.clearTimeout(t)
                    })
                }
                e.requestAnimationFrame = r, e.cancelAnimationFrame = o
            }, function (t, e) {
                t.exports = function (t, e, a, i) {
                    var n, r = t = t || {}, o = typeof t.default;
                    "object" !== o && "function" !== o || (n = t, r = t.default);
                    var s = "function" == typeof r ? r.options : r;
                    if (e && (s.render = e.render, s.staticRenderFns = e.staticRenderFns), a && (s._scopeId = a), i) {
                        var c = Object.create(s.computed || null);
                        Object.keys(i).forEach((function (t) {
                            var e = i[t];
                            c[t] = function () {
                                return e
                            }
                        })), s.computed = c
                    }
                    return {esModule: n, exports: r, options: s}
                }
            }, function (t, e) {
                t.exports = {
                    render: function () {
                        var t = this, e = t.$createElement;
                        return (t._self._c || e)("span", [t._v("\n  " + t._s(t.displayValue) + "\n")])
                    }, staticRenderFns: []
                }
            }])
        }))
    }
}]);