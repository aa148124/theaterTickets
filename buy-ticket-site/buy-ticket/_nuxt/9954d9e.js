(window.webpackJsonp=window.webpackJsonp||[]).push([[3],{346:function(t,e,n){t.exports=function(){"use strict";var t=1e3,e=6e4,n=36e5,r="millisecond",i="second",s="minute",u="hour",a="day",o="week",l="month",c="quarter",f="year",d="date",h="Invalid Date",m=/^(\d{4})[-/]?(\d{1,2})?[-/]?(\d{0,2})[Tt\s]*(\d{1,2})?:?(\d{1,2})?:?(\d{1,2})?[.:]?(\d+)?$/,v=/\[([^\]]+)]|Y{1,4}|M{1,4}|D{1,2}|d{1,4}|H{1,2}|h{1,2}|a|A|m{1,2}|s{1,2}|Z{1,2}|SSS/g,$={name:"en",weekdays:"Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),months:"January_February_March_April_May_June_July_August_September_October_November_December".split("_"),ordinal:function(t){var e=["th","st","nd","rd"],n=t%100;return"["+t+(e[(n-20)%10]||e[n]||e[0])+"]"}},y=function(t,e,n){var r=String(t);return!r||r.length>=e?t:""+Array(e+1-r.length).join(n)+t},D={s:y,z:function(t){var e=-t.utcOffset(),n=Math.abs(e),r=Math.floor(n/60),i=n%60;return(e<=0?"+":"-")+y(r,2,"0")+":"+y(i,2,"0")},m:function t(e,n){if(e.date()<n.date())return-t(n,e);var r=12*(n.year()-e.year())+(n.month()-e.month()),i=e.clone().add(r,l),s=n-i<0,u=e.clone().add(r+(s?-1:1),l);return+(-(r+(n-i)/(s?i-u:u-i))||0)},a:function(t){return t<0?Math.ceil(t)||0:Math.floor(t)},p:function(t){return{M:l,y:f,w:o,d:a,D:d,h:u,m:s,s:i,ms:r,Q:c}[t]||String(t||"").toLowerCase().replace(/s$/,"")},u:function(t){return void 0===t}},g="en",M={};M[g]=$;var p=function(t){return t instanceof O},_=function t(e,n,r){var i;if(!e)return g;if("string"==typeof e){var s=e.toLowerCase();M[s]&&(i=s),n&&(M[s]=n,i=s);var u=e.split("-");if(!i&&u.length>1)return t(u[0])}else{var a=e.name;M[a]=e,i=a}return!r&&i&&(g=i),i||!r&&g},S=function(t,e){if(p(t))return t.clone();var n="object"==typeof e?e:{};return n.date=t,n.args=arguments,new O(n)},w=D;w.l=_,w.i=p,w.w=function(t,e){return S(t,{locale:e.$L,utc:e.$u,x:e.$x,$offset:e.$offset})};var O=function(){function $(t){this.$L=_(t.locale,null,!0),this.parse(t)}var y=$.prototype;return y.parse=function(t){this.$d=function(t){var e=t.date,n=t.utc;if(null===e)return new Date(NaN);if(w.u(e))return new Date;if(e instanceof Date)return new Date(e);if("string"==typeof e&&!/Z$/i.test(e)){var r=e.match(m);if(r){var i=r[2]-1||0,s=(r[7]||"0").substring(0,3);return n?new Date(Date.UTC(r[1],i,r[3]||1,r[4]||0,r[5]||0,r[6]||0,s)):new Date(r[1],i,r[3]||1,r[4]||0,r[5]||0,r[6]||0,s)}}return new Date(e)}(t),this.$x=t.x||{},this.init()},y.init=function(){var t=this.$d;this.$y=t.getFullYear(),this.$M=t.getMonth(),this.$D=t.getDate(),this.$W=t.getDay(),this.$H=t.getHours(),this.$m=t.getMinutes(),this.$s=t.getSeconds(),this.$ms=t.getMilliseconds()},y.$utils=function(){return w},y.isValid=function(){return!(this.$d.toString()===h)},y.isSame=function(t,e){var n=S(t);return this.startOf(e)<=n&&n<=this.endOf(e)},y.isAfter=function(t,e){return S(t)<this.startOf(e)},y.isBefore=function(t,e){return this.endOf(e)<S(t)},y.$g=function(t,e,n){return w.u(t)?this[e]:this.set(n,t)},y.unix=function(){return Math.floor(this.valueOf()/1e3)},y.valueOf=function(){return this.$d.getTime()},y.startOf=function(t,e){var n=this,r=!!w.u(e)||e,c=w.p(t),h=function(t,e){var i=w.w(n.$u?Date.UTC(n.$y,e,t):new Date(n.$y,e,t),n);return r?i:i.endOf(a)},m=function(t,e){return w.w(n.toDate()[t].apply(n.toDate("s"),(r?[0,0,0,0]:[23,59,59,999]).slice(e)),n)},v=this.$W,$=this.$M,y=this.$D,D="set"+(this.$u?"UTC":"");switch(c){case f:return r?h(1,0):h(31,11);case l:return r?h(1,$):h(0,$+1);case o:var g=this.$locale().weekStart||0,M=(v<g?v+7:v)-g;return h(r?y-M:y+(6-M),$);case a:case d:return m(D+"Hours",0);case u:return m(D+"Minutes",1);case s:return m(D+"Seconds",2);case i:return m(D+"Milliseconds",3);default:return this.clone()}},y.endOf=function(t){return this.startOf(t,!1)},y.$set=function(t,e){var n,o=w.p(t),c="set"+(this.$u?"UTC":""),h=(n={},n[a]=c+"Date",n[d]=c+"Date",n[l]=c+"Month",n[f]=c+"FullYear",n[u]=c+"Hours",n[s]=c+"Minutes",n[i]=c+"Seconds",n[r]=c+"Milliseconds",n)[o],m=o===a?this.$D+(e-this.$W):e;if(o===l||o===f){var v=this.clone().set(d,1);v.$d[h](m),v.init(),this.$d=v.set(d,Math.min(this.$D,v.daysInMonth())).$d}else h&&this.$d[h](m);return this.init(),this},y.set=function(t,e){return this.clone().$set(t,e)},y.get=function(t){return this[w.p(t)]()},y.add=function(r,c){var d,h=this;r=Number(r);var m=w.p(c),v=function(t){var e=S(h);return w.w(e.date(e.date()+Math.round(t*r)),h)};if(m===l)return this.set(l,this.$M+r);if(m===f)return this.set(f,this.$y+r);if(m===a)return v(1);if(m===o)return v(7);var $=(d={},d[s]=e,d[u]=n,d[i]=t,d)[m]||1,y=this.$d.getTime()+r*$;return w.w(y,this)},y.subtract=function(t,e){return this.add(-1*t,e)},y.format=function(t){var e=this,n=this.$locale();if(!this.isValid())return n.invalidDate||h;var r=t||"YYYY-MM-DDTHH:mm:ssZ",i=w.z(this),s=this.$H,u=this.$m,a=this.$M,o=n.weekdays,l=n.months,c=function(t,n,i,s){return t&&(t[n]||t(e,r))||i[n].slice(0,s)},f=function(t){return w.s(s%12||12,t,"0")},d=n.meridiem||function(t,e,n){var r=t<12?"AM":"PM";return n?r.toLowerCase():r},m={YY:String(this.$y).slice(-2),YYYY:this.$y,M:a+1,MM:w.s(a+1,2,"0"),MMM:c(n.monthsShort,a,l,3),MMMM:c(l,a),D:this.$D,DD:w.s(this.$D,2,"0"),d:String(this.$W),dd:c(n.weekdaysMin,this.$W,o,2),ddd:c(n.weekdaysShort,this.$W,o,3),dddd:o[this.$W],H:String(s),HH:w.s(s,2,"0"),h:f(1),hh:f(2),a:d(s,u,!0),A:d(s,u,!1),m:String(u),mm:w.s(u,2,"0"),s:String(this.$s),ss:w.s(this.$s,2,"0"),SSS:w.s(this.$ms,3,"0"),Z:i};return r.replace(v,(function(t,e){return e||m[t]||i.replace(":","")}))},y.utcOffset=function(){return 15*-Math.round(this.$d.getTimezoneOffset()/15)},y.diff=function(r,d,h){var m,v=w.p(d),$=S(r),y=($.utcOffset()-this.utcOffset())*e,D=this-$,g=w.m(this,$);return g=(m={},m[f]=g/12,m[l]=g,m[c]=g/3,m[o]=(D-y)/6048e5,m[a]=(D-y)/864e5,m[u]=D/n,m[s]=D/e,m[i]=D/t,m)[v]||D,h?g:w.a(g)},y.daysInMonth=function(){return this.endOf(l).$D},y.$locale=function(){return M[this.$L]},y.locale=function(t,e){if(!t)return this.$L;var n=this.clone(),r=_(t,e,!0);return r&&(n.$L=r),n},y.clone=function(){return w.w(this.$d,this)},y.toDate=function(){return new Date(this.valueOf())},y.toJSON=function(){return this.isValid()?this.toISOString():null},y.toISOString=function(){return this.$d.toISOString()},y.toString=function(){return this.$d.toUTCString()},$}(),Y=O.prototype;return S.prototype=Y,[["$ms",r],["$s",i],["$m",s],["$H",u],["$W",a],["$M",l],["$y",f],["$D",d]].forEach((function(t){Y[t[1]]=function(e){return this.$g(e,t[0],t[1])}})),S.extend=function(t,e){return t.$i||(t(e,O,S),t.$i=!0),S},S.locale=_,S.isDayjs=p,S.unix=function(t){return S(1e3*t)},S.en=M[g],S.Ls=M,S.p={},S}()},348:function(t,e,n){"use strict";n.d(e,"a",(function(){return o})),n.d(e,"b",(function(){return l}));var r=n(10);function o(t){return Object(r.a)({url:"/arrange/getArrangeByFilmId/".concat(t),method:"get"})}function l(t){return Object(r.a)({url:"/arrange/getArrangeById/".concat(t),method:"get"})}},373:function(t,e,n){"use strict";n.r(e);var r=n(144),o=n(348),l=n(346),c=n.n(l),f=n(11),d=n.n(f),h={data:function(){return{filmId:"",film:{},imgSrc:"",tableData:[]}},created:function(){this.filmId=this.$route.params.buy,this.init()},methods:{init:function(){var t=this;r.a(this.filmId).then((function(e){t.film=e.data,t.film.releaseTime=c()(t.film.releaseTime).format("YYYY-MM-DD HH:mm:ss"),t.imgSrc=t.film.poster})),o.a(this.filmId).then((function(e){t.tableData=e.data;for(var i=0;i<t.tableData.length;i++)t.tableData[i].playTime=c()(t.tableData[i].playTime).format("YYYY-MM-DD HH:mm:ss")}))},toSeat:function(t){if(d.a.get("token")){var e=this.tableData[t];window.location.href="/seat?filmId="+this.film.id+"&arrangeId="+e.id}else loginEvent.$emit("loginDialogEvent")}}},m=n(13),component=Object(m.a)(h,(function(){var t=this,e=t._self._c;return e("div",[e("div",{staticClass:"details"},[e("div",[e("img",{attrs:{id:"filmImg",src:t.imgSrc}})]),t._v(" "),e("div",[e("div",[e("div",{staticStyle:{"font-size":"26px"}},[t._v(t._s(t.film.filmName))])]),t._v(" "),e("div",[e("div",[t._v("主演："+t._s(t.film.performer))])]),t._v(" "),e("div",[e("div",[t._v("导演："+t._s(t.film.director))])]),t._v(" "),e("div",[e("div",[t._v("制片国家/地区："+t._s(t.film.region))])]),t._v(" "),e("div",[e("div",[t._v(t._s(t.film.releaseTime)+"中国大陆上映")])]),t._v(" "),e("div",[e("div",{staticClass:"plotIntroduction"},[t._v("剧情介绍:"+t._s(t.film.synopsis))])])])]),t._v(" "),e("div",{staticClass:"selectTime"},[e("div",{staticStyle:{width:"1200px"}},[e("el-table",{staticStyle:{width:"100%","font-size":"16px"},attrs:{data:t.tableData,stripe:""}},[e("el-table-column",{attrs:{prop:"playTime",label:"放映时间",width:"240",align:"center"}}),t._v(" "),e("el-table-column",{attrs:{prop:"room.roomName",label:"放映厅",width:"240",align:"center"}}),t._v(" "),e("el-table-column",{attrs:{prop:"price",label:"售价(元)",width:"240",align:"center"}}),t._v(" "),e("el-table-column",{attrs:{prop:"stock",label:"剩余座位",width:"240",align:"center"}}),t._v(" "),e("el-table-column",{attrs:{align:"center",label:"选座购票",width:"240"},scopedSlots:t._u([{key:"default",fn:function(n){return[e("el-button",{staticStyle:{width:"80px"},attrs:{type:"danger",round:"",size:"small"},on:{click:function(e){return t.toSeat(n.$index)}}},[t._v("选座购票")])]}}])})],1)],1)])])}),[],!1,null,null,null);e.default=component.exports}}]);