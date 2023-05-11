<template>
  <div style="margin-bottom: 100px; margin-top: 20px">
    <div>
      <el-steps :active="1" align-center>
        <el-step title="选择影片场次"></el-step>
        <el-step title="选择座位"></el-step>
        <el-step title="15分钟内付款"></el-step>
        <el-step title="影院取票观影"></el-step>
      </el-steps>
    </div>
    <div class="seat">
      <div class="seat-size">
        <div class="seatleft">
          <div class="seatleft-top">
            <div>
              <img
                src="
              https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%8F%AF%E9%80%89.png"
              /><span>可选座位</span>
            </div>
            <div>
              <img
                src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%B2%E5%94%AE.png"
              /><span>已售座位</span>
            </div>
            <div>
              <img
                src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%B2%E9%80%89.png"
              /><span>已选座位</span>
            </div>
          </div>
          <div class="seat-bottom">
            <div class="seat-bottom-left">
              <div v-for="index in 12" :key="index">
                <div>{{ index }}</div>
              </div>
            </div>
            <div>
              <div>
                <img
                  src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E9%93%B6%E5%B9%95.png"
                />
              </div>
              <div
                style="
                  justify-content: center;
                  display: flex;
                  margin-bottom: 50px;
                "
              >
                银幕中央
              </div>
              <div>
                <div
                  v-for="(i, index_i) in seat"
                  :key="index_i + '-label'"
                  style="justify-content: center; display: flex"
                >
                  <div v-for="(j, index_j) in i" :key="index_j + '-label2'">
                    <input
                      type="image"
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%8F%AF%E9%80%89.png"
                      style="margin: 0px 5px"
                      v-if="j == '0'"
                      @click="selectSeat(index_i, index_j)"
                    />
                    <input
                      type="image"
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%B2%E5%94%AE.png"
                      style="margin: 0px 5px"
                      disabled
                      v-else-if="j == '1'"
                    />
                    <input
                      type="image"
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%B2%E9%80%89.png"
                      style="margin: 0px 5px"
                      v-else-if="j == '2'"
                      @click="selectSeat(index_i, index_j)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="seatright">
          <div style="display: flex">
            <div>
              <img :src="film.poster" />
            </div>
            <div style="margin-left: 20px">
              <div
                style="
                  font-size: 20px;
                  font-weight: 700;
                  color: #333;
                  margin: 0 0 14px;
                "
              >
                {{ film.filmName }}
              </div>
              <div>导演：{{ film.director }}</div>
              <div>演员：{{ film.performer }}</div>
              <div>时长：{{ film.duration }}分钟</div>
            </div>
          </div>
          <div style="margin-top: 20px">
            <div style="margin-bottom: 10px">
              <!-- 影厅：{{ arrange.room.roomName }} -->
            </div>
            <div style="margin-bottom: 10px">场次：{{ arrange.playTime }}</div>
            <div style="margin-bottom: 10px">票价：{{ arrange.price }}/张</div>
          </div>
          <hr style="border: 1px dashed #e5e5e5" />
          <div style="height: 160px">
            <div>座位：一次最多选4个座位</div>
            <div style="height: 120px">{{ order.seat }}</div>
            <div>总价：{{ order.price }}元</div>
          </div>
          <hr style="border: 1px dashed #e5e5e5" />
          <div style="justify-content: center; display: flex; margin-top: 20px">
            <div>
              <div style="margin: 30px">手机号：{{ user.phone }}</div>
              <div style="margin: 30px">
                <el-button type="danger" round @click="toPay()"
                  >确认选座</el-button
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import cookie from "js-cookie";
import pay from "@/pages/buy/pay";
import * as orderApi from "@/api/order";
import * as filmApi from "@/api/film";
import * as buyApi from "@/api/buy";
export default {
  data() {
    return {
      filmId: "",
      film: {},
      imgSrc: "",
      tableData: [],
      arrange: {},
      seat: [],
      selected: "",
      price: 0,
      count: 0,
      order: {
        arrangeId: "",
        seat: "",
        price: 0,
      },
      user: {},
    };
  },
  created() {
    // this.film = this.$route.params.film;
    // this.arrange = this.$route.params.arrange;
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      let token = cookie.get("token");
      if (token) {
        this.user = JSON.parse(cookie.get("user"));
      }
      let url = window.location.href;
      this.filmId = url.split("&")[0].split("=")[1];
      this.order.arrangeId = url.split("&")[1].split("=")[1];

      filmApi.getFilmById(this.filmId).then((res) => {
        if (res.code == 200) {
          this.film = res.data;
        }
      });
      buyApi.getArrangeById(this.order.arrangeId).then((res) => {
        if (res.code == 200) {
          this.arrange = res.data;
          // this.seat = this.arrange.seat.split(",");
          this.seat = JSON.parse(this.arrange.seat);
          this.order.arrangeId = this.arrange.id;
        }
      });
    },
    toPay() {
      orderApi.addOrder(this.order).then((res) => {
        if (res.code == 200) {
          window.location.href = "/buy/pay?orderId=" + res.data.id;
        }
      });
    },
    selectSeat(i, j) {
      let img =
        document.getElementsByTagName("input")[i * this.seat[i].length + j + 1];
      if (this.seat[i][j] == 0) {
        if (this.count == 4) {
          this.$message.error("最多只能选4个座为哦");
          return;
        }
        this.count++;
        //价格
        this.order.price += this.arrange.price;
        //已选座位
        this.order.seat += i + 1 + "排" + (j + 1) + "座 ";
        console.log(this.order.seat);
        //改变座位的值
        this.seat[i][j] = 2;
        img.src =
          "https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%B2%E9%80%89.png";
      } else {
        this.count--;
        this.order.price -= this.arrange.price;
        let temp = i + 1 + "排" + (j + 1) + "座 ";
        this.order.seat = this.order.seat.replace(temp, "");
        this.seat[i][j] = 0;
        img.src =
          "https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%8F%AF%E9%80%89.png";
      }
    },
  },
};
</script>
