(window.webpackJsonp=window.webpackJsonp||[]).push([[7],{346:function(t,e,n){t.exports=function(){"use strict";var t=1e3,e=6e4,n=36e5,r="millisecond",i="second",s="minute",u="hour",a="day",o="week",c="month",l="quarter",f="year",h="date",d="Invalid Date",v=/^(\d{4})[-/]?(\d{1,2})?[-/]?(\d{0,2})[Tt\s]*(\d{1,2})?:?(\d{1,2})?:?(\d{1,2})?[.:]?(\d+)?$/,m=/\[([^\]]+)]|Y{1,4}|M{1,4}|D{1,2}|d{1,4}|H{1,2}|h{1,2}|a|A|m{1,2}|s{1,2}|Z{1,2}|SSS/g,y={name:"en",weekdays:"Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),months:"January_February_March_April_May_June_July_August_September_October_November_December".split("_"),ordinal:function(t){var e=["th","st","nd","rd"],n=t%100;return"["+t+(e[(n-20)%10]||e[n]||e[0])+"]"}},$=function(t,e,n){var r=String(t);return!r||r.length>=e?t:""+Array(e+1-r.length).join(n)+t},_={s:$,z:function(t){var e=-t.utcOffset(),n=Math.abs(e),r=Math.floor(n/60),i=n%60;return(e<=0?"+":"-")+$(r,2,"0")+":"+$(i,2,"0")},m:function t(e,n){if(e.date()<n.date())return-t(n,e);var r=12*(n.year()-e.year())+(n.month()-e.month()),i=e.clone().add(r,c),s=n-i<0,u=e.clone().add(r+(s?-1:1),c);return+(-(r+(n-i)/(s?i-u:u-i))||0)},a:function(t){return t<0?Math.ceil(t)||0:Math.floor(t)},p:function(t){return{M:c,y:f,w:o,d:a,D:h,h:u,m:s,s:i,ms:r,Q:l}[t]||String(t||"").toLowerCase().replace(/s$/,"")},u:function(t){return void 0===t}},g="en",S={};S[g]=y;var p=function(t){return t instanceof D},w=function t(e,n,r){var i;if(!e)return g;if("string"==typeof e){var s=e.toLowerCase();S[s]&&(i=s),n&&(S[s]=n,i=s);var u=e.split("-");if(!i&&u.length>1)return t(u[0])}else{var a=e.name;S[a]=e,i=a}return!r&&i&&(g=i),i||!r&&g},x=function(t,e){if(p(t))return t.clone();var n="object"==typeof e?e:{};return n.date=t,n.args=arguments,new D(n)},M=_;M.l=w,M.i=p,M.w=function(t,e){return x(t,{locale:e.$L,utc:e.$u,x:e.$x,$offset:e.$offset})};var D=function(){function y(t){this.$L=w(t.locale,null,!0),this.parse(t)}var $=y.prototype;return $.parse=function(t){this.$d=function(t){var e=t.date,n=t.utc;if(null===e)return new Date(NaN);if(M.u(e))return new Date;if(e instanceof Date)return new Date(e);if("string"==typeof e&&!/Z$/i.test(e)){var r=e.match(v);if(r){var i=r[2]-1||0,s=(r[7]||"0").substring(0,3);return n?new Date(Date.UTC(r[1],i,r[3]||1,r[4]||0,r[5]||0,r[6]||0,s)):new Date(r[1],i,r[3]||1,r[4]||0,r[5]||0,r[6]||0,s)}}return new Date(e)}(t),this.$x=t.x||{},this.init()},$.init=function(){var t=this.$d;this.$y=t.getFullYear(),this.$M=t.getMonth(),this.$D=t.getDate(),this.$W=t.getDay(),this.$H=t.getHours(),this.$m=t.getMinutes(),this.$s=t.getSeconds(),this.$ms=t.getMilliseconds()},$.$utils=function(){return M},$.isValid=function(){return!(this.$d.toString()===d)},$.isSame=function(t,e){var n=x(t);return this.startOf(e)<=n&&n<=this.endOf(e)},$.isAfter=function(t,e){return x(t)<this.startOf(e)},$.isBefore=function(t,e){return this.endOf(e)<x(t)},$.$g=function(t,e,n){return M.u(t)?this[e]:this.set(n,t)},$.unix=function(){return Math.floor(this.valueOf()/1e3)},$.valueOf=function(){return this.$d.getTime()},$.startOf=function(t,e){var n=this,r=!!M.u(e)||e,l=M.p(t),d=function(t,e){var i=M.w(n.$u?Date.UTC(n.$y,e,t):new Date(n.$y,e,t),n);return r?i:i.endOf(a)},v=function(t,e){return M.w(n.toDate()[t].apply(n.toDate("s"),(r?[0,0,0,0]:[23,59,59,999]).slice(e)),n)},m=this.$W,y=this.$M,$=this.$D,_="set"+(this.$u?"UTC":"");switch(l){case f:return r?d(1,0):d(31,11);case c:return r?d(1,y):d(0,y+1);case o:var g=this.$locale().weekStart||0,S=(m<g?m+7:m)-g;return d(r?$-S:$+(6-S),y);case a:case h:return v(_+"Hours",0);case u:return v(_+"Minutes",1);case s:return v(_+"Seconds",2);case i:return v(_+"Milliseconds",3);default:return this.clone()}},$.endOf=function(t){return this.startOf(t,!1)},$.$set=function(t,e){var n,o=M.p(t),l="set"+(this.$u?"UTC":""),d=(n={},n[a]=l+"Date",n[h]=l+"Date",n[c]=l+"Month",n[f]=l+"FullYear",n[u]=l+"Hours",n[s]=l+"Minutes",n[i]=l+"Seconds",n[r]=l+"Milliseconds",n)[o],v=o===a?this.$D+(e-this.$W):e;if(o===c||o===f){var m=this.clone().set(h,1);m.$d[d](v),m.init(),this.$d=m.set(h,Math.min(this.$D,m.daysInMonth())).$d}else d&&this.$d[d](v);return this.init(),this},$.set=function(t,e){return this.clone().$set(t,e)},$.get=function(t){return this[M.p(t)]()},$.add=function(r,l){var h,d=this;r=Number(r);var v=M.p(l),m=function(t){var e=x(d);return M.w(e.date(e.date()+Math.round(t*r)),d)};if(v===c)return this.set(c,this.$M+r);if(v===f)return this.set(f,this.$y+r);if(v===a)return m(1);if(v===o)return m(7);var y=(h={},h[s]=e,h[u]=n,h[i]=t,h)[v]||1,$=this.$d.getTime()+r*y;return M.w($,this)},$.subtract=function(t,e){return this.add(-1*t,e)},$.format=function(t){var e=this,n=this.$locale();if(!this.isValid())return n.invalidDate||d;var r=t||"YYYY-MM-DDTHH:mm:ssZ",i=M.z(this),s=this.$H,u=this.$m,a=this.$M,o=n.weekdays,c=n.months,l=function(t,n,i,s){return t&&(t[n]||t(e,r))||i[n].slice(0,s)},f=function(t){return M.s(s%12||12,t,"0")},h=n.meridiem||function(t,e,n){var r=t<12?"AM":"PM";return n?r.toLowerCase():r},v={YY:String(this.$y).slice(-2),YYYY:this.$y,M:a+1,MM:M.s(a+1,2,"0"),MMM:l(n.monthsShort,a,c,3),MMMM:l(c,a),D:this.$D,DD:M.s(this.$D,2,"0"),d:String(this.$W),dd:l(n.weekdaysMin,this.$W,o,2),ddd:l(n.weekdaysShort,this.$W,o,3),dddd:o[this.$W],H:String(s),HH:M.s(s,2,"0"),h:f(1),hh:f(2),a:h(s,u,!0),A:h(s,u,!1),m:String(u),mm:M.s(u,2,"0"),s:String(this.$s),ss:M.s(this.$s,2,"0"),SSS:M.s(this.$ms,3,"0"),Z:i};return r.replace(m,(function(t,e){return e||v[t]||i.replace(":","")}))},$.utcOffset=function(){return 15*-Math.round(this.$d.getTimezoneOffset()/15)},$.diff=function(r,h,d){var v,m=M.p(h),y=x(r),$=(y.utcOffset()-this.utcOffset())*e,_=this-y,g=M.m(this,y);return g=(v={},v[f]=g/12,v[c]=g,v[l]=g/3,v[o]=(_-$)/6048e5,v[a]=(_-$)/864e5,v[u]=_/n,v[s]=_/e,v[i]=_/t,v)[m]||_,d?g:M.a(g)},$.daysInMonth=function(){return this.endOf(c).$D},$.$locale=function(){return S[this.$L]},$.locale=function(t,e){if(!t)return this.$L;var n=this.clone(),r=w(t,e,!0);return r&&(n.$L=r),n},$.clone=function(){return M.w(this.$d,this)},$.toDate=function(){return new Date(this.valueOf())},$.toJSON=function(){return this.isValid()?this.toISOString():null},$.toISOString=function(){return this.$d.toISOString()},$.toString=function(){return this.$d.toUTCString()},y}(),C=D.prototype;return x.prototype=C,[["$ms",r],["$s",i],["$m",s],["$H",u],["$W",a],["$M",c],["$y",f],["$D",h]].forEach((function(t){C[t[1]]=function(e){return this.$g(e,t[0],t[1])}})),x.extend=function(t,e){return t.$i||(t(e,D,x),t.$i=!0),x},x.locale=w,x.isDayjs=p,x.unix=function(t){return x(1e3*t)},x.en=S[g],x.Ls=S,x.p={},x}()},374:function(t,e,n){"use strict";n.r(e);n(52);var r=n(346),o=n.n(r),c=n(144),l={data:function(){return{poster:[],filmShowList:[],filmShowCount:"",filmRanking:[],filmFutureList:[],filmFutureCount:""}},methods:{getShowFilm:function(){var t=this;c.f().then((function(e){200==e.code&&(t.filmShowList=e.data,t.filmShowList.length>8&&(t.filmShowList=t.filmShowList.slice(0,8)))}))},getShowFilmCount:function(){var t=this;c.g().then((function(e){200==e.code&&(t.filmShowCount=e.data)}))},getFutureFilm:function(){var t=this;c.b().then((function(e){if(200==e.code){t.filmFutureList=e.data,t.filmFutureList.length>8&&(t.filmFutureList=t.filmFutureList.slice(0,8));for(var i=0;i<t.filmFutureList.length;i++)t.filmFutureList[i].releaseTime=o()(t.filmFutureList[i].releaseTime).format("MM月DD日")}}))},getFutureFilmCount:function(){var t=this;c.c().then((function(e){200==e.code&&(t.filmFutureCount=e.data)}))},getRanking:function(){var t=this;c.e().then((function(e){200==e.code&&(t.filmRanking=e.data)}))},toBuy:function(t){window.location.href="/buy/"+t},getPoster:function(){var t=this;c.d().then((function(e){200==e.code&&(t.poster=e.data)}))}},created:function(){this.getShowFilm(),this.getShowFilmCount(),this.getFutureFilm(),this.getFutureFilmCount(),this.getRanking(),this.getPoster()}},f=n(13),component=Object(f.a)(l,(function(){var t=this,e=t._self._c;return e("div",[e("div",{staticClass:"nav"},[e("div",{staticClass:"block"},[e("el-carousel",{attrs:{height:"450px"}},t._l(t.poster,(function(t){return e("el-carousel-item",{key:t},[e("img",{staticStyle:{width:"1000px",height:"450px"},attrs:{src:t}})])})),1)],1)]),t._v(" "),e("div",{staticClass:"show"},[e("div",{staticStyle:{width:"800px",height:"1000px"}},[e("div",{staticClass:"type"},[e("span",{staticStyle:{width:"740px","font-size":"26px"}},[t._v("\n          正在热映（"+t._s(t.filmShowCount)+"部）\n        ")]),t._v(" "),e("span",{staticStyle:{float:"right","margin-top":"13px"}},[e("el-link",{staticStyle:{"text-decoration":"none"},attrs:{type:"danger"},on:{click:function(e){return t.$router.push({name:"filmInfo",params:{activeIndex:"1"}})}}},[e("span",[t._v("全部")])]),e("i",{staticClass:"el-icon-arrow-right",staticStyle:{"font-size":"14px"}})],1)]),t._v(" "),e("div",{staticClass:"panel"},t._l(t.filmShowList,(function(n){return e("div",{key:n.id},[e("div",{staticClass:"film"},[e("a",{attrs:{href:"/filmInfo/"+n.id}},[e("img",{attrs:{src:n.poster}})]),t._v(" "),e("div",[e("el-button",{on:{click:function(e){return t.toBuy(n.id)}}},[t._v("购 票")])],1),t._v(" "),e("div",{staticClass:"filmName"},[t._v(t._s(n.filmName))])])])})),0),t._v(" "),e("div",{staticClass:"type"},[e("span",{staticStyle:{width:"740px","font-size":"26px",color:"#2d98f3"}},[t._v("\n          即将上映（"+t._s(t.filmFutureCount)+"部）\n        ")]),t._v(" "),e("span",{staticStyle:{float:"right","margin-top":"13px"}},[e("el-link",{staticStyle:{"text-decoration":"none"},attrs:{type:"primary"},on:{click:function(e){return t.$router.push({name:"filmInfo",params:{activeIndex:"2"}})}}},[e("span",{staticStyle:{color:"#2d98f3"}},[t._v("全部")])]),e("i",{staticClass:"el-icon-arrow-right",staticStyle:{color:"#2d98f3"}})],1)]),t._v(" "),e("div",{staticClass:"panel"},t._l(t.filmFutureList,(function(n){return e("div",{key:n.id},[e("div",{staticClass:"film"},[e("a",{attrs:{href:"/filmInfo/"+n.id}},[e("img",{attrs:{src:n.poster}})]),t._v(" "),e("div",{staticStyle:{width:"160px"}},[e("el-card",{staticStyle:{height:"40px"},attrs:{shadow:"hover"}},[e("span",{staticStyle:{color:"#ffb400",margin:"10px"}},[t._v("99999人想看")])])],1),t._v(" "),e("div",{staticClass:"filmName"},[t._v(t._s(n.filmName))])]),t._v(" "),e("div",[e("span",{staticStyle:{margin:"40px"}},[t._v(t._s(n.releaseTime)+"上映")])])])})),0)]),t._v(" "),e("div",{staticClass:"ranking"},[t._m(0),t._v(" "),t._l(t.filmRanking,(function(n,r){return e("div",{key:r},[0==r?e("div",{staticStyle:{"margin-top":"20px",width:"360px"}},[e("a",{staticStyle:{"text-decoration":"none"},attrs:{href:"/filmInfo/"+n.id}},[e("div",{staticStyle:{display:"flex",height:"78px"}},[e("div",[e("img",{attrs:{src:n.poster}})]),t._v(" "),e("el-card",{staticStyle:{"border-radius":"0px",width:"100%"},attrs:{shadow:"hover"}},[e("div",{staticStyle:{"font-size":"18px","padding-left":"5px"}},[t._v("\n                  "+t._s(n.filmName)+"\n                ")]),t._v(" "),e("div",{staticStyle:{"font-size":"14px",color:"#ef4238","padding-left":"5px"}},[t._v("\n                  "+t._s(n.boxOffice)+"元\n                ")])])],1)])]):t._e(),t._v(" "),r>0?e("div",{staticStyle:{"margin-top":"10px",width:"360px"}},[e("a",{staticStyle:{"text-decoration":"none"},attrs:{href:"/filmInfo/"+n.id}},[e("div",{staticStyle:{display:"flex",height:"55px"}},[e("el-card",{staticStyle:{"border-radius":"0px",width:"100%",border:"0px"},attrs:{shadow:"hover"}},[1==r||2==r?e("span",{staticStyle:{"font-size":"18px",color:"#ef4238","font-style":"italic"}},[t._v(t._s(r+1))]):t._e(),t._v(" "),r>2?e("span",{staticStyle:{"font-size":"18px",color:"#999","font-style":"italic"}},[t._v(t._s(r+1))]):t._e(),t._v(" "),e("span",{staticStyle:{"font-size":"16px","padding-left":"5px"}},[t._v(t._s(n.filmName))]),t._v(" "),e("span",{staticStyle:{"font-size":"14px",color:"#ef4238",float:"right"}},[t._v(t._s(n.boxOffice)+"元")])])],1)])]):t._e()])}))],2)])])}),[function(){var t=this._self._c;return t("div",[t("span",{staticStyle:{width:"740px","font-size":"26px",color:"#ef4238"}},[this._v("\n          历史票房\n        ")])])}],!1,null,null,null);e.default=component.exports}}]);