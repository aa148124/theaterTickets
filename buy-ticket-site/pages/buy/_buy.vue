<template>
  <div>
    <div class="details">
      <div>
        <img id="filmImg" :src="imgSrc" />
      </div>
      <div>
        <div>
          <div style="font-size: 26px">{{ film.filmName }}</div>
        </div>
        <div>
          <div>主演：{{ film.performer }}</div>
        </div>
        <div>
          <div>导演：{{ film.director }}</div>
        </div>
        <div>
          <div>制片国家/地区：{{ film.region }}</div>
        </div>
        <div>
          <div>{{ film.releaseTime }}中国大陆上映</div>
        </div>
        <div>
          <div class="plotIntroduction">剧情介绍:{{ film.synopsis }}</div>
        </div>
      </div>
    </div>
    <div class="selectTime">
      <div style="width: 1200px">
        <el-table :data="tableData" stripe style="width: 100%; font-size: 16px">
          <el-table-column
            prop="playTime"
            label="放映时间"
            width="240"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="room.roomName"
            label="放映厅"
            width="240"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="price"
            label="售价(元)"
            width="240"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="stock"
            label="剩余座位"
            width="240"
            align="center"
          >
          </el-table-column>
          <el-table-column align="center" label="选座购票" width="240">
            <template slot-scope="scope">
              <el-button
                type="danger"
                round
                style="width: 80px"
                size="small"
                @click="toSeat(scope.$index)"
                >选座购票</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>
<script>
import * as filmApi from "@/api/film";
import * as buyApi from "@/api/buy";
import dayjs from "dayjs";
import cookie from "js-cookie";
export default {
  data() {
    return {
      filmId: "",
      film: {},
      imgSrc: "",
      tableData: [],
    };
  },
  created() {
    this.filmId = this.$route.params.buy;
    this.init();
  },
  methods: {
    init() {
      filmApi.getFilmById(this.filmId).then((res) => {
        this.film = res.data;
        this.film.releaseTime = dayjs(this.film.releaseTime).format(
          "YYYY-MM-DD HH:mm:ss"
        );
        this.imgSrc = this.film.poster;
      });
      buyApi.getArrangeByFilmId(this.filmId).then((res) => {
        this.tableData = res.data;
        for (let i = 0; i < this.tableData.length; i++) {
          this.tableData[i].playTime = dayjs(this.tableData[i].playTime).format(
            "YYYY-MM-DD HH:mm:ss"
          );
        }
      });
    },
    toSeat(index) {
      let token = cookie.get("token");
      if (!token) {
        loginEvent.$emit("loginDialogEvent");
        return;
      }
      let arrange = this.tableData[index];
      // this.$router.push({
      //   name: "seat",
      //   params: { film: this.film, arrange: arrange },
      // });
      window.location.href =
        "/seat?filmId=" + this.film.id + "&arrangeId=" + arrange.id;
    },
  },
};
</script>
