<template>
  <div style="margin-bottom: 100px; margin-top: 20px">
    <div>
      <el-steps :active="2" align-center>
        <el-step title="选择影片场次"></el-step>
        <el-step title="选择座位"></el-step>
        <el-step title="15分钟内付款"></el-step>
        <el-step title="影院取票观影"></el-step>
      </el-steps>
    </div>
    <div style="justify-content: center; display: flex; margin: 40px 0 40px 0">
      <div class="pay-mid">
        <div style="margin: 40px; display: flex">
          <div style="margin-right: 20px">
            <img
              src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/b681f7e04b084228b25153d9e91003a4.jpg"
            />
          </div>
          <div>
            <div style="margin-bottom: 10px">
              请在{{ parseInt(sum / 60) }}分钟{{ sum % 60 }}秒内完成支付
            </div>
            <div style="color: #f03d37">
              超时订单会自动取消，如遇支付问题，请致电客服:6666-666
            </div>
          </div>
        </div>
      </div>
    </div>
    <div style="justify-content: center; display: flex; margin: 10px 0 10px 0">
      <div style="width: 1200px">
        请仔细核对场次信息，出票后将<span style="color: #faaf00"
          >无法退票和改签</span
        >
      </div>
    </div>
    <div style="justify-content: center; display: flex">
      <div style="width: 1200px">
        <el-table :data="tableData" border style="width: 100%">
          <el-table-column prop="filmName" label="影片" align="center">
          </el-table-column>
          <el-table-column prop="date" label="时间" align="center">
          </el-table-column>
          <el-table-column prop="roomName" label="影厅" align="center">
          </el-table-column>
          <el-table-column prop="seat" label="座位" align="center">
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div style="justify-content: center; display: flex; margin-top: 100px">
      <div style="width: 1200px">
        <div style="float: right">实际支付:{{ order.price }}元</div>
      </div>
    </div>
    <div style="justify-content: center; display: flex; margin-top: 20px">
      <div style="width: 1200px">
        <div style="float: right">
          <el-button type="danger" round @click="submit()">确认支付</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import * as buyApi from "@/api/buy";
import * as orderApi from "@/api/order";
export default {
  data() {
    return {
      tableData: [
        {
          filmName: "",
          date: "",
          roomName: "",
          seat: "",
        },
      ],
      orderId: "",
      sum: 0,
      order: {},
      arrangeId: "",
      arrange: {},
      filmName: "",
      pay: {
        id: "",
      },
    };
  },
  created() {
    this.Time();
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      let url = window.location.href;
      this.orderId = url.split("=")[1];
      orderApi.getOrderById(this.orderId).then((res) => {
        if (res.code == 200) {
          this.order = res.data.order;
          this.sum = res.data.time;
          console.log(this.order);
          this.arrangeId = this.order.arrangeId;
          this.tableData[0].seat = this.order.seat;
          console.log(this.tableData);
          buyApi.getArrangeById(this.arrangeId).then((res) => {
            if (res.code == 200) {
              this.tableData[0].roomName = res.data.room.roomName;
              this.tableData[0].filmName = res.data.film.filmName;
              this.tableData[0].date = res.data.playTime;

              console.log(res.data);
            }
          });
        }
      });
    },
    submit() {
      this.pay.id = this.orderId;
      orderApi
        .pay(this.pay)
        .then((res) => {
          if (res.code == 200) {
            this.$message.success("支付成功");
            this.$router.push({
              name: "profile",
              params: { activeIndex: "1" },
            });
          }
        })
        .catch((res) => {
          this.$message.error(res.msg);
        });
    },

    //定时器没过1秒参数减1
    Time() {
      setInterval(() => {
        if (this.sum > 0) {
          this.sum -= 1;
        }
      }, 1000);
    },
  },
};
</script>
