<template>
  <div>
    <div class="nav">
      <div class="block">
        <el-carousel height="450px">
          <el-carousel-item v-for="item in poster" :key="item">
            <img :src="item" style="width: 1000px; height: 450px" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div class="show">
      <div style="width: 800px; height: 1000px">
        <div class="type">
          <span style="width: 740px; font-size: 26px">
            正在热映（{{ filmShowCount }}部）
          </span>
          <span style="float: right; margin-top: 13px"
            ><el-link
              type="danger"
              @click="
                $router.push({ name: 'filmInfo', params: { activeIndex: '1' } })
              "
              style="text-decoration: none"
              ><span>全部</span></el-link
            ><i class="el-icon-arrow-right" style="font-size: 14px"></i
          ></span>
        </div>

        <div class="panel">
          <div v-for="film in filmShowList" :key="film.id">
            <div class="film">
              <a :href="'/filmInfo/' + film.id">
                <img :src="film.poster" />
              </a>
              <div><el-button @click="toBuy(film.id)">购 票</el-button></div>
              <div class="filmName">{{ film.filmName }}</div>
            </div>
          </div>
        </div>

        <div class="type">
          <span style="width: 740px; font-size: 26px; color: #2d98f3">
            即将上映（{{ filmFutureCount }}部）
          </span>
          <span style="float: right; margin-top: 13px"
            ><el-link
              type="primary"
              @click="
                $router.push({ name: 'filmInfo', params: { activeIndex: '2' } })
              "
              style="text-decoration: none"
              ><span style="color: #2d98f3">全部</span></el-link
            ><i class="el-icon-arrow-right" style="color: #2d98f3"></i
          ></span>
        </div>
        <div class="panel">
          <div v-for="film in filmFutureList" :key="film.id">
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

      <div class="ranking">
        <div>
          <span style="width: 740px; font-size: 26px; color: #ef4238">
            历史票房
          </span>
        </div>
        <div v-for="(ranking, index) in filmRanking" :key="index">
          <div style="margin-top: 20px; width: 360px" v-if="index == 0">
            <a :href="'/filmInfo/' + ranking.id" style="text-decoration: none">
              <div style="display: flex; height: 78px">
                <div>
                  <img :src="ranking.poster" />
                </div>
                <el-card shadow="hover" style="border-radius: 0px; width: 100%">
                  <div style="font-size: 18px; padding-left: 5px">
                    {{ ranking.filmName }}
                  </div>
                  <div
                    style="font-size: 14px; color: #ef4238; padding-left: 5px"
                  >
                    {{ ranking.boxOffice }}元
                  </div>
                </el-card>
              </div>
            </a>
          </div>
          <div style="margin-top: 10px; width: 360px" v-if="index > 0">
            <a :href="'/filmInfo/' + ranking.id" style="text-decoration: none">
              <div style="display: flex; height: 55px">
                <el-card
                  shadow="hover"
                  style="border-radius: 0px; width: 100%; border: 0px"
                >
                  <span
                    style="font-size: 18px; color: #ef4238; font-style: italic"
                    v-if="index == 1 || index == 2"
                    >{{ index + 1 }}</span
                  >
                  <span
                    style="font-size: 18px; color: #999; font-style: italic"
                    v-if="index > 2"
                    >{{ index + 1 }}</span
                  >
                  <span style="font-size: 16px; padding-left: 5px">{{
                    ranking.filmName
                  }}</span>
                  <span style="font-size: 14px; color: #ef4238; float: right"
                    >{{ ranking.boxOffice }}元</span
                  >
                </el-card>
              </div>
            </a>
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
      poster: [],
      filmShowList: [],
      filmShowCount: "",
      filmRanking: [],
      filmFutureList: [],
      filmFutureCount: "",
    };
  },
  methods: {
    getShowFilm() {
      filmApi.getShowFilm().then((res) => {
        if (res.code == 200) {
          this.filmShowList = res.data;
          if (this.filmShowList.length > 8) {
            this.filmShowList = this.filmShowList.slice(0, 8);
          }
        }
      });
    },
    getShowFilmCount() {
      filmApi.getShowFilmCount().then((res) => {
        if (res.code == 200) {
          this.filmShowCount = res.data;
        }
      });
    },
    getFutureFilm() {
      filmApi.getFutureFilm().then((res) => {
        if (res.code == 200) {
          this.filmFutureList = res.data;
          if (this.filmFutureList.length > 8) {
            this.filmFutureList = this.filmFutureList.slice(0, 8);
          }
          for (let i = 0; i < this.filmFutureList.length; i++) {
            this.filmFutureList[i].releaseTime = dayjs(
              this.filmFutureList[i].releaseTime
            ).format("MM月DD日");
          }
        }
      });
    },
    getFutureFilmCount() {
      filmApi.getFutureFilmCount().then((res) => {
        if (res.code == 200) {
          this.filmFutureCount = res.data;
        }
      });
    },
    getRanking() {
      filmApi.getRanking().then((res) => {
        if (res.code == 200) {
          this.filmRanking = res.data;
        }
      });
    },
    toBuy(filmId) {
      window.location.href = "/buy/" + filmId;
    },
    getPoster() {
      filmApi.getPoster().then((res) => {
        if (res.code == 200) {
          this.poster = res.data;
        }
      });
    },
  },
  created() {
    this.getShowFilm();
    this.getShowFilmCount();
    this.getFutureFilm();
    this.getFutureFilmCount();
    this.getRanking();
    this.getPoster();
  },
};
</script>
