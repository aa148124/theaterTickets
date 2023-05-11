<template>
  <div style="margin-bottom: 100px">
    <div class="show-top">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#47464a"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="1">正在热映</el-menu-item>
        <el-menu-item index="2">即将上映</el-menu-item>
      </el-menu>
    </div>
    <div style="justify-content: center; display: flex">
      <div class="showfilm">
        <div class="panel">
          <div
            v-for="film in filmList"
            :key="'a' + film.id"
            v-if="activeIndex == '1'"
          >
            <div class="film">
              <a :href="'/filmInfo/' + film.id">
                <img :src="film.poster" />
              </a>
              <div><el-button @click="toBuy(film.id)">购 票</el-button></div>
              <div class="filmName">{{ film.filmName }}</div>
            </div>
          </div>
          <div
            v-for="film in filmList"
            :key="'b' + film.id"
            v-if="activeIndex == '2' || activeIndex == '3'"
          >
            <div class="film">
              <a :href="'/filmInfo/' + film.id">
                <img :src="film.poster" />
              </a>
              <div style="width: 160px">
                <el-card shadow="hover" style="height: 40px">
                  <span style="color: #ffb400; margin: 10px">99999人想看</span>
                </el-card>
              </div>
              <div class="filmName">{{ film.filmName }}</div>
            </div>
            <div>
              <span style="margin: 40px">{{ film.releaseTime }}上映</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import dayjs from "dayjs";
import * as filmApi from "@/api/film";
export default {
  data() {
    return {
      activeIndex: "1",
      filmList: [],
    };
  },
  created() {
    this.activeIndex = this.$route.params.activeIndex;
    this.filmList = this.$route.params.film;
    if (this.activeIndex == "1") {
      this.getShowFilm();
    } else if (this.activeIndex == "2") {
      this.getFutureFilm();
    }
    // for (let i = 0; i < this.filmList.length; i++) {
    //   this.filmList[i].releaseTime = dayjs(this.filmList[i].releaseTime).format(
    //     "MM月DD日"
    //   );
    // }
    console.log(this.filmList);
  },
  methods: {
    handleSelect(key, keyPath) {
      this.activeIndex = key;
      if (key == "1") {
        this.getShowFilm();
      } else {
        this.getFutureFilm();
      }
    },
    getShowFilm() {
      filmApi.getShowFilm().then((res) => {
        if (res.code == 200) {
          this.filmList = res.data;
        }
      });
    },
    toBuy(filmId) {
      window.location.href = "/buy/" + filmId;
    },
    getFutureFilm() {
      filmApi.getFutureFilm().then((res) => {
        if (res.code == 200) {
          this.filmList = res.data;
          for (let i = 0; i < this.filmList.length; i++) {
            this.filmList[i].releaseTime = dayjs(
              this.filmList[i].releaseTime
            ).format("MM月DD日");
          }
        }
      });
    },
  },
};
</script>
