(window.webpackJsonp=window.webpackJsonp||[]).push([[8],{347:function(t,e,r){"use strict";r.d(e,"d",(function(){return l})),r.d(e,"c",(function(){return d})),r.d(e,"a",(function(){return v})),r.d(e,"b",(function(){return f})),r.d(e,"e",(function(){return m}));var n=r(10),o=r(349),c=r.n(o);function l(t,e,r){return Object(n.a)({url:"/order/getOrdersByUserId/".concat(t,"/").concat(e,"/").concat(r),method:"get"})}function d(t){return Object(n.a)({url:"/order/getOrderTotalByUserId/".concat(t),method:"get"})}function v(t){return Object(n.a)({url:"/order/addOrder",method:"post",data:t})}function f(t){return Object(n.a)({url:"/order/getOrderById/".concat(t),method:"get"})}function m(data){return Object(n.a)({url:"/order/pay",method:"put",headers:{"content-type":"application/x-www-form-urlencoded"},data:c.a.stringify(data)})}},352:function(t,e){},355:function(t,e,r){var content=r(368);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[t.i,content,""]]),content.locals&&(t.exports=content.locals);(0,r(27).default)("502a9b4c",content,!0,{sourceMap:!1})},367:function(t,e,r){"use strict";r(355)},368:function(t,e,r){var n=r(26)(!1);n.push([t.i,"\n.profile-left .el-menu-item.is-active {\r\n  background-color: #ed3931 !important;\n}\r\n",""]),t.exports=n},371:function(t,e,r){"use strict";r.r(e);r(208);var n=r(11),o=r.n(n),c=r(139),l=r(347),d={data:function(){return{activeIndex:"1",filmList:[],imageUrl:"",labelPosition:"right",user:{id:"",username:"",nickName:"",phone:"",createTime:"",icon:""},orders:[],currentPage:1,size:3,total:12,pay:{id:""}}},created:function(){this.activeIndex=this.$route.params.activeIndex,this.showInfo()},mounted:function(){this.handleCurrentChange(),this.getOrderTotalByUserId()},methods:{submitForm:function(){var t=this;new RegExp("^1[3578]\\d{9}$").test(this.user.phone)?c.c(this.user).then((function(e){200==e.code?(o.a.set("user",JSON.stringify(t.user),{domain:"localhost"}),t.$message.success("保存成功")):(console.log(e),t.$message.error("保存失败"))})):this.$message.error("手机号格式有误")},handleSelect:function(t){console.log(t),this.activeIndex=t},showInfo:function(){o.a.get("token")&&(this.user=JSON.parse(o.a.get("user"))),console.log("index"+this.activeIndex)},handleAvatarSuccess:function(t){console.log(t),this.user.icon=t.data},handleSizeChange:function(t){console.log("每页 ".concat(t," 条"))},handleCurrentChange:function(){var t=this;l.d(this.user.id,this.currentPage,this.size).then((function(e){console.log(e.data),t.orders=e.data}))},getOrderTotalByUserId:function(){var t=this;l.c(this.user.id).then((function(e){t.total=e.data}))},submit:function(t){var e=this;this.pay.id=t,l.e(this.pay).then((function(t){200==t.code&&(e.$message.success("支付成功"),window.location.reload())})).catch((function(t){e.$message.error(t.msg)}))}}},v=(r(367),r(13)),component=Object(v.a)(d,(function(){var t=this,e=t._self._c;return e("div",{staticStyle:{"margin-bottom":"100px"}},[e("div",{staticStyle:{"justify-content":"center",display:"flex"}},[e("div",{staticClass:"profile-size"},[e("div",{staticClass:"profile-left"},[t._m(0),t._v(" "),e("div",[e("div",[e("el-menu",{staticClass:"el-menu-vertical-demo",attrs:{"default-active":t.activeIndex,"background-color":"#f4f3f4","text-color":"#333333","active-text-color":"#ffffff"},on:{select:t.handleSelect}},[e("el-menu-item",{attrs:{index:"1"}},[e("span",[t._v("我的订单")])]),t._v(" "),e("el-menu-item",{attrs:{index:"2"}},[e("span",[t._v("基本信息")])])],1)],1)])]),t._v(" "),"1"==t.activeIndex?e("div",{staticClass:"profile-right"},[e("div",{staticClass:"profile-right-top"},[t._v("我的订单")]),t._v(" "),t._l(t.orders,(function(r){return e("div",{key:r.id,staticClass:"film-card"},[e("div",{staticClass:"film-card-top"},[e("div",{staticStyle:{display:"flex",padding:"16px 20px"}},[e("div",{staticStyle:{"margin-right":"30px"}},[t._v(t._s(r.orderTime))]),t._v(" "),e("div",[t._v("猫眼订单号:"+t._s(r.id))])])]),t._v(" "),e("div",{staticStyle:{padding:"20px 0 20px 20px",display:"flex"}},[e("div",[e("img",{attrs:{src:r.arrange.film.poster}})]),t._v(" "),e("div",{staticStyle:{width:"421px"}},[e("div",{staticClass:"order-content"},[t._v("\n                《"+t._s(r.arrange.film.filmName)+"》\n              ")]),t._v(" "),e("div",{staticStyle:{"font-size":"12px",color:"#999","margin-bottom":"4px"}},[t._v("\n                "+t._s(r.arrange.room.roomName)+" "+t._s(r.seat)+"\n              ")]),t._v(" "),e("div",{staticStyle:{"font-size":"12px",color:"#999","margin-bottom":"4px"}},[t._v("\n                "+t._s(r.arrange.playTime)+"\n              ")])]),t._v(" "),e("div",{staticClass:"order-price"},[t._v(t._s(r.price)+"元")]),t._v(" "),0==r.status?e("div",{staticClass:"order-price"},[t._v("已取消")]):t._e(),t._v(" "),1==r.status?e("div",{staticClass:"order-price"},[t._v("未支付")]):t._e(),t._v(" "),2==r.status?e("div",{staticClass:"order-price"},[t._v("已完成")]):t._e(),t._v(" "),1==r.status?e("div",{staticClass:"order-price",staticStyle:{"text-align":"center"}},[e("el-button",{staticStyle:{width:"100px"},attrs:{type:"danger",round:"",size:"mini"},on:{click:function(e){return t.submit(r.id)}}},[t._v("立即支付")])],1):e("div",{staticClass:"order-price",staticStyle:{"text-align":"center"}},[t._v("\n              查看详情\n            ")])])])})),t._v(" "),e("div",{staticStyle:{"margin-top":"50px"}},[e("el-pagination",{attrs:{"current-page":t.currentPage,"page-size":t.size,layout:"total, prev, pager, next",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange,"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e}}})],1)],2):e("div",{staticClass:"profile-right"},[e("div",{staticClass:"profile-right-top"},[t._v("基本信息")]),t._v(" "),e("div",{staticStyle:{display:"flex"}},[e("div",[e("div",[e("img",{staticStyle:{width:"258px",height:"258px"},attrs:{src:t.user.icon}})]),t._v(" "),e("div",{staticStyle:{margin:"50px 0 0 50px"}},[e("el-upload",{staticClass:"upload-demo",attrs:{action:"http://192.168.88.1:1111/upload/fileUpload","list-type":"picture","on-success":t.handleAvatarSuccess}},[e("el-button",{attrs:{size:"small",type:"danger"}},[t._v("点击上传")]),t._v(" "),e("div",{staticClass:"el-upload__tip",attrs:{slot:"tip"},slot:"tip"},[t._v("\n                  只能上传jpg/png文件，且不超过500kb\n                ")])],1)],1)]),t._v(" "),e("div",{staticStyle:{"margin-left":"100px"}},[e("el-form",{attrs:{"label-position":t.labelPosition,"label-width":"80px",model:t.user}},[e("el-form-item",{attrs:{label:"用户id"}},[e("el-input",{attrs:{disabled:!0},model:{value:t.user.id,callback:function(e){t.$set(t.user,"id",e)},expression:"user.id"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"用户名"}},[e("el-input",{attrs:{disabled:!0},model:{value:t.user.username,callback:function(e){t.$set(t.user,"username",e)},expression:"user.username"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"昵称"}},[e("el-input",{model:{value:t.user.nickName,callback:function(e){t.$set(t.user,"nickName",e)},expression:"user.nickName"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"手机号"}},[e("el-input",{model:{value:t.user.phone,callback:function(e){t.$set(t.user,"phone",e)},expression:"user.phone"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"注册时间"}},[e("el-input",{attrs:{disabled:!0},model:{value:t.user.createTime,callback:function(e){t.$set(t.user,"createTime",e)},expression:"user.createTime"}})],1),t._v(" "),e("el-form-item",[e("el-button",{attrs:{type:"danger"},on:{click:function(e){return t.submitForm()}}},[t._v("保存")])],1)],1)],1)])])])])])}),[function(){var t=this._self._c;return t("div",{staticClass:"profile-left-top"},[t("div",{staticStyle:{"margin-top":"40px","margin-bottom":"30px"}},[t("span",[this._v("个人中心")])])])}],!1,null,null,null);e.default=component.exports}}]);