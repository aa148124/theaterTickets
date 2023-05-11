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
    <div class="comment">
      <div style="width: 1100px">
        <div class="title">
          热门短评
          <el-button
            size="small"
            round
            style="float: right; width: 80px"
            @click="shortComment"
            >写短评</el-button
          >
        </div>
        <div
          style="display: flex; margin-top: 20px"
          v-for="comment in commentList"
          :key="comment.id"
        >
          <div style="margin-right: 20px">
            <el-avatar :size="50" :src="comment.icon"></el-avatar>
          </div>
          <div>
            <div>
              {{ comment.nickName }}
            </div>
            <div style="width: 1000px">
              <span>
                <span>{{ comment.commentTime }}</span>
                <div style="display: flex">
                  <div
                    v-for="(score, index) in comment.score"
                    :key="index + '-only'"
                  >
                    <img
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%A6%E4%BA%AE.png"
                      v-if="index % 2 == 0"
                    />
                    <img
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%8F%B3%E4%BA%AE.png"
                      v-if="index % 2 == 1"
                    />
                  </div>
                  <div v-if="comment.score % 2 == 1">
                    <img
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%8F%B3%E7%81%B0.png"
                    />
                  </div>
                  <div
                    v-for="index in parseInt((10 - comment.score) / 2)"
                    :key="index"
                  >
                    <img
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%A6%E7%81%B0.png"
                    /><img
                      src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%8F%B3%E7%81%B0.png"
                    />
                  </div>
                </div>
              </span>
              <span style="float: right" v-if="comment.isLiked">
                <img
                  src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E5%B7%B2%E8%B5%9E.png"
                  @click="liked(comment.id)"
                />{{ comment.liked }}
              </span>
              <span style="float: right" v-else>
                <img
                  src="https://dengkunhou.oss-cn-guangzhou.aliyuncs.com/%E6%9C%AA%E8%B5%9E.png"
                  @click="liked(comment.id)"
                />{{ comment.liked }}
              </span>
            </div>
            <div
              style="margin-top: 20px; padding-bottom: 20px; font-size: 16px"
            >
              {{ comment.content }}
            </div>
            <hr style="border: 1px solid #e5e5e5" />
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <div style="display: flex; justify-content: center">
        <div>
          <el-rate v-model="value" show-text allow-half> </el-rate>
        </div>
      </div>
      <div style="margin-top: 30px">
        <el-input
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4 }"
          placeholder="请输入内容"
          v-model="comment.content"
        >
        </el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="dialogVisible = false"
          size="small"
          style="width: 60px"
          >取 消</el-button
        >
        <el-button
          type="primary"
          @click="addComment()"
          size="small"
          style="width: 60px"
          >提 交</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import * as filmApi from "@/api/film";
import * as commentApi from "@/api/comment";
import dayjs from "dayjs";
import cookie from "js-cookie";
export default {
  data() {
    return {
      filmId: "",
      film: {},
      imgSrc: "",
      commentList: [],
      dialogVisible: false,
      value: 0,
      comment: {
        userId: "",
        filmId: "",
        content: "",
        score: "",
      },
    };
  },
  created() {
    this.filmId = this.$route.params.filmInfo;
    this.init();
    this.getHotComment();
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
    },
    getHotComment() {
      commentApi.getHotComment(this.filmId).then((res) => {
        this.commentList = res.data;
      });
    },
    liked(commentId) {
      let token = cookie.get("token");
      let userId = cookie.get("userId");
      if (!token) {
        loginEvent.$emit("loginDialogEvent");
        return;
      }
      commentApi.liked(commentId, userId).then((res) => {
        this.getHotComment();
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    addComment() {
      //获取用户id
      let userId = cookie.get("userId");
      this.comment.score = this.value * 2;
      this.comment.filmId = this.filmId;
      this.comment.userId = userId;
      commentApi.addComment(this.comment).then((res) => {
        if (res.code == 200) {
          this.$message.success("提交成功");
          this.dialogVisible = false;
          this.getHotComment();
        }
      });
    },
    shortComment() {
      let token = cookie.get("token");
      if (!token) {
        loginEvent.$emit("loginDialogEvent");
        return;
      }
      this.dialogVisible = true;
    },
  },
};
</script>
