<template>
  <div style="display: flex; justify-content: center; margin: 50px">
    <div style="width: 50%">
      <el-form :model="film">
        <el-form-item label="海报" :label-width="formLabelWidth">
          <el-upload
            class="avatar-uploader"
            action="http://192.168.88.1:1111/upload/fileUpload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="电影名称" :label-width="formLabelWidth">
          <el-input v-model="film.filmName"> </el-input>
        </el-form-item>
        <el-form-item label="导演" :label-width="formLabelWidth">
          <el-input v-model="film.director"> </el-input>
        </el-form-item>
        <el-form-item label="主演" :label-width="formLabelWidth">
          <el-input v-model="film.performer"> </el-input>
        </el-form-item>
        <el-form-item label="地区" :label-width="formLabelWidth">
          <el-input v-model="film.region"> </el-input>
        </el-form-item>
        <el-form-item label="上映时间" :label-width="formLabelWidth">
          <div class="block">
            <el-date-picker
              v-model="film.releaseTime"
              type="datetime"
              placeholder="选择日期时间"
            >
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="时长(分钟)" :label-width="formLabelWidth">
          <el-input v-model="film.duration"> </el-input>
        </el-form-item>
        <el-form-item label="剧情简介" :label-width="formLabelWidth">
          <el-input v-model="film.synopsis"> </el-input>
        </el-form-item>
        <el-form-item label="电影状态" :label-width="formLabelWidth">
          <el-select v-model="film.status" placeholder="请选择电影状态">
            <el-option label="正在热映" value="0"></el-option>
            <el-option label="即将上映" value="1"></el-option>
            <el-option label="已经下架" value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="resetFilm">重 置</el-button>
      </div>
    </div>
  </div>
</template>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 240px;
  height: 330px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 240px;
  height: 330px;
  display: block;
}
</style>
<script>
import * as filmset from "@/api/filmset";
import * as dayjs from "dayjs";
export default {
  data() {
    return {
      formLabelWidth: "100px",
      film: {
        id: "",
        poster: "",
        filmName: "",
        nickName: "",
        director: "",
        performer: "",
        boxOffice: "",
        region: "",
        releaseTime: "",
        duration: "",
        synopsis: "",
        status: "",
      },
      options: [
        {
          value: "0",
          label: "正在热映",
        },
        {
          value: "1",
          label: "即将上映",
        },
        {
          value: "2",
          label: "已经下架",
        },
      ],
      imageUrl: "",
    };
  },
  created() {},
  methods: {
    //重置
    resetFilm() {
      this.film = {
        id: "",
        poster: "",
        filmName: "",
        nickName: "",
        director: "",
        performer: "",
        region: "",
        releaseTime: "",
        duration: "",
        synopsis: "",
        status: "",
      };
    },
    submitForm() {
      this.film.releaseTime = dayjs(this.film.releaseTime).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      console.log(this.film);
      filmset.addFilm(this.film).then((res) => {
        if (res.code == 200) {
          this.$message.success("添加成功");
          this.resetFilm();
          this.imageUrl = "";
        }
      });
    },
    handleAvatarSuccess(res, file) {
      console.log(res);
      this.imageUrl = URL.createObjectURL(file.raw);
      this.film.poster = res.data;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
  },
};
</script>
