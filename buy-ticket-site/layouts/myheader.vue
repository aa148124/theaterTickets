<template>
  <div class="nav">
    <div>
      <img
        src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/b2a5736acd654a99badd8ca26cabf45d.jpg"
        id="logo"
      />
    </div>
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
    >
      <el-menu-item index="/" @click="$router.push({ name: 'index' })"
        >首页</el-menu-item
      >
      <el-menu-item
        index="/filmInfo/filmShow"
        @click="
          $router.push({ name: 'filmInfo', params: { activeIndex: '1' } })
        "
        >电影</el-menu-item
      >
      <el-menu-item index="orders.html" @click="toOrder()"
        >我的订单</el-menu-item
      >
      <el-submenu index="5">
        <template slot="title" v-if="userId == ''"
          ><img
            src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E6%9C%AA%E7%99%BB%E5%BD%95.png"
            style="width: 40px; height: 40px"
        /></template>
        <el-menu-item
          index="login"
          @click="dialogUserFormVisible = true"
          v-if="userId == ''"
          id="login"
          >登录/注册</el-menu-item
        >

        <template slot="title" v-if="userId != ''"
          ><img :src="user.icon" style="width: 40px; height: 40px"
        /></template>
        <el-menu-item
          v-if="userId != ''"
          @click="
            $router.push({ name: 'profile', params: { activeIndex: '2' } })
          "
          >个人中心</el-menu-item
        >
        <el-menu-item index="logout" v-if="userId != ''" @click="logout()"
          >退出</el-menu-item
        >
      </el-submenu>
    </el-menu>
    <!--        搜索框-->
    <div style="margin-top: 10px; width: 240px">
      <div style="display: flex">
        <el-input
          placeholder="找影视剧、影人"
          v-model="input"
          class="input-with-select"
        >
        </el-input>
        <el-button
          slot="append"
          icon="el-icon-search"
          style="background-color: #f5f7fa; width: 20%"
          @click="search()"
        ></el-button>
      </div>
    </div>

    <!-- 登录弹出层 -->
    <el-dialog
      :visible.sync="dialogUserFormVisible"
      style="text-align: left"
      top="50px"
      :append-to-body="true"
      width="960px"
      @close="closeDialog()"
      id="loginDialog"
    >
      <phoneLogin
        :rule-form="phoneForm"
        @send="send"
        @submit="submit"
        @errHandle="errHandle"
      ></phoneLogin>
    </el-dialog>
  </div>
</template>
<style>
.el-input {
  width: 130px;
}
</style>
<script>
import cookie from "js-cookie";
import Vue from "vue";

import PhoneLogin from "../components/PhoneLogin.vue";
import * as userApi from "@/api/user";
import * as filmApi from "@/api/film";
export default {
  name: "Login",
  components: { PhoneLogin },
  props: {},
  data() {
    return {
      activeIndex: "/",
      input: "",
      dialogUserFormVisible: false,
      phoneForm: {
        phone: "",
        code: "",
      },
      user: {
        id: "",
        username: "",
        nickName: "",
        phone: "",
        createTime: "",
        updateTime: "",
        icon: "",
      },
      userId: "",
    };
  },
  mounted() {
    // 注册全局登录事件对象
    window.loginEvent = new Vue();
    // 监听登录事件
    loginEvent.$on("loginDialogEvent", function () {
      document.getElementById("login").click();
    });
  },
  methods: {
    handleSelect(key) {
      this.activeIndex = key;
    },
    search() {
      filmApi.search(this.input).then((res) => {
        if (res.code == 200) {
          this.$router.push({
            name: "filmInfo",
            params: { activeIndex: "3", film: res.data },
          });
        }
      });
    },
    submit(loginVo) {
      userApi.login(loginVo).then((res) => {
        this.user = res.data.user;
        this.setCookies(res.data.user, res.data.token);
        this.$message.success("登录成功");
      });
    },
    setCookies(user, token) {
      console.log(this.user.icon);
      cookie.set("token", token, { domain: "localhost" });
      cookie.set("user", JSON.stringify(user), { domain: "localhost" });
      cookie.set("userId", user.id, { domain: "localhost" });
      window.location.reload();
    },
    showInfo() {
      let token = cookie.get("token");
      console.log(token);
      if (token) {
        this.userId = cookie.get("userId");
        this.user = JSON.parse(cookie.get("user"));
      }
    },
    errHandle() {
      this.$message.error("表单填写有误，请检查");
      console.log("失败");
    },
    send(phone) {
      userApi.send(phone).then((res) => {
        if (res.code == 200) {
          this.$message.success("发送验证码成功");
        }
      });
    },
    // 关闭登录层
    closeDialog() {
      if (this.clearSmsTime) {
        clearInterval(this.clearSmsTime);
      }
    },

    toOrder() {
      let token = cookie.get("token");
      if (!token) {
        this.dialogUserFormVisible = true;
        return;
      }
      this.$router.push({ name: "profile", params: { activeIndex: "1" } });
    },
    logout() {
      cookie.remove("token");
      cookie.remove("user");
      this.$router.push({ name: "index" });
      window.location.reload();
    },
  },
  created() {
    this.showInfo();
  },
};
</script>
