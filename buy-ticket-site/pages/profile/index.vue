<template>
  <div style="margin-bottom: 100px">
    <div style="justify-content: center; display: flex">
      <div class="profile-size">
        <div class="profile-left">
          <div class="profile-left-top">
            <div style="margin-top: 40px; margin-bottom: 30px">
              <span>个人中心</span>
            </div>
          </div>
          <div>
            <div>
              <el-menu
                :default-active="activeIndex"
                class="el-menu-vertical-demo"
                background-color="#f4f3f4"
                text-color="#333333"
                active-text-color="#ffffff"
                @select="handleSelect"
              >
                <el-menu-item index="1">
                  <span>我的订单</span>
                </el-menu-item>
                <el-menu-item index="2">
                  <span>基本信息</span>
                </el-menu-item>
              </el-menu>
            </div>
          </div>
        </div>
        <div class="profile-right" v-if="activeIndex == '1'">
          <div class="profile-right-top">我的订单</div>
          <div class="film-card" v-for="order in orders" :key="order.id">
            <div class="film-card-top">
              <div style="display: flex; padding: 16px 20px">
                <div style="margin-right: 30px">{{ order.orderTime }}</div>
                <div>订单号:{{ order.id }}</div>
              </div>
            </div>
            <div style="padding: 20px 0 20px 20px; display: flex">
              <div>
                <img :src="order.arrange.film.poster" />
              </div>
              <div style="width: 421px">
                <div class="order-content">
                  《{{ order.arrange.film.filmName }}》
                </div>
                <div style="font-size: 12px; color: #999; margin-bottom: 4px">
                  {{ order.arrange.room.roomName }} {{ order.seat }}
                </div>
                <div style="font-size: 12px; color: #999; margin-bottom: 4px">
                  {{ order.arrange.playTime }}
                </div>
              </div>
              <div class="order-price">{{ order.price }}元</div>
              <div class="order-price" v-if="order.status == 0">已取消</div>
              <div class="order-price" v-if="order.status == 1">未支付</div>
              <div class="order-price" v-if="order.status == 2">已完成</div>
              <div
                class="order-price"
                style="text-align: center"
                v-if="order.status == 1"
              >
                <el-button
                  type="danger"
                  round
                  size="mini"
                  style="width: 100px"
                  @click="submit(order.id)"
                  >立即支付</el-button
                >
              </div>
              <div class="order-price" style="text-align: center" v-else>
                查看详情
              </div>
            </div>
          </div>

          <div style="margin-top: 50px">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page.sync="currentPage"
              :page-size="size"
              layout="total, prev, pager, next"
              :total="total"
            >
            </el-pagination>
          </div>
        </div>
        <div class="profile-right" v-else>
          <div class="profile-right-top">基本信息</div>
          <div style="display: flex">
            <div>
              <div>
                <img :src="user.icon" style="width: 258px; height: 258px" />
              </div>
              <div style="margin: 50px 0 0 50px">
                <el-upload
                  class="upload-demo"
                  action="http://192.168.88.1:1111/upload/fileUpload"
                  list-type="picture"
                  :on-success="handleAvatarSuccess"
                >
                  <el-button size="small" type="danger">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">
                    只能上传jpg/png文件，且不超过500kb
                  </div>
                </el-upload>
              </div>
            </div>
            <div style="margin-left: 100px">
              <el-form
                :label-position="labelPosition"
                label-width="80px"
                :model="user"
              >
                <el-form-item label="用户id">
                  <el-input v-model="user.id" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                  <el-input v-model="user.username" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="昵称">
                  <el-input v-model="user.nickName"></el-input>
                </el-form-item>
                <el-form-item label="手机号">
                  <el-input v-model="user.phone"></el-input>
                </el-form-item>
                <el-form-item label="注册时间">
                  <el-input
                    v-model="user.createTime"
                    :disabled="true"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="danger" @click="submitForm()"
                    >保存</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style>
.profile-left .el-menu-item.is-active {
  background-color: #ed3931 !important;
}
</style>
<script>
import cookie from "js-cookie";
import * as userApi from "@/api/user";
import * as orderApi from "@/api/order";
export default {
  data() {
    return {
      activeIndex: "1",
      filmList: [],
      imageUrl: "",
      labelPosition: "right",
      user: {
        id: "",
        username: "",
        nickName: "",
        phone: "",
        createTime: "",
        icon: "",
      },
      orders: [],
      currentPage: 1,
      size: 3,
      total: 12,
      pay: {
        id: "",
      },
    };
  },
  created() {
    this.activeIndex = this.$route.params.activeIndex;
    this.showInfo();
  },
  mounted() {
    this.handleCurrentChange();
    this.getOrderTotalByUserId();
  },
  methods: {
    submitForm() {
      var regExp = new RegExp("^1[3578]\\d{9}$");
      if (!regExp.test(this.user.phone)) {
        this.$message.error("手机号格式有误");
        return;
      }
      userApi.updateUser(this.user).then((res) => {
        if (res.code == 200) {
          //更新cookie
          cookie.set("user", JSON.stringify(this.user), {
            domain: "localhost",
          });
          this.$message.success("保存成功");
        } else {
          console.log(res);
          this.$message.error("保存失败");
        }
      });
    },
    handleSelect(key) {
      console.log(key);
      this.activeIndex = key;
    },
    showInfo() {
      let token = cookie.get("token");
      if (token) {
        this.user = JSON.parse(cookie.get("user"));
      }
      console.log("index" + this.activeIndex);
    },
    handleAvatarSuccess(res) {
      console.log(res);
      // this.imageUrl = URL.createObjectURL(file.raw);
      this.user.icon = res.data;
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange() {
      orderApi
        .getOrdersByUserId(this.user.id, this.currentPage, this.size)
        .then((res) => {
          console.log(res.data);
          this.orders = res.data;
        });
    },
    getOrderTotalByUserId() {
      orderApi.getOrderTotalByUserId(this.user.id).then((res) => {
        this.total = res.data;
      });
    },
    submit(id) {
      this.pay.id = id;
      orderApi
        .pay(this.pay)
        .then((res) => {
          if (res.code == 200) {
            this.$message.success("支付成功");
            window.location.reload();
          }
        })
        .catch((res) => {
          this.$message.error(res.msg);
        });
    },
  },
};
</script>
