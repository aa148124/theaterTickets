<template>
  <div style="margin: 50px">
    <!--搜索表单-->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline">
      <el-form-item label="电影id">
        <el-input v-model="searchObj.id" placeholder="id"></el-input>
      </el-form-item>
      <el-form-item label="电影名称">
        <el-input
          v-model="searchObj.filmName"
          placeholder="电影名称"
        ></el-input>
      </el-form-item>
      <el-select v-model="searchObj.status" placeholder="电影状态">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="
        tableData.filter(
          (data) =>
            !search || data.name.toLowerCase().includes(search.toLowerCase())
        )
      "
      style="width: 100%"
    >
      <el-table-column align="center" label="id" prop="id"> </el-table-column>
      <el-table-column align="center" label="海报地址" prop="poster">
        <template scope="scope">
          <el-image :src="scope.row.poster" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="电影名称" prop="filmName">
      </el-table-column>
      <el-table-column
        show-overflow-tooltip
        align="center"
        label="导演"
        prop="director"
      >
      </el-table-column>
      <el-table-column
        show-overflow-tooltip
        align="center"
        label="主演"
        prop="performer"
      >
      </el-table-column>
      <el-table-column align="center" label="票房" prop="boxOffice">
      </el-table-column>
      <el-table-column align="center" label="地区" prop="region">
      </el-table-column>
      <el-table-column align="center" label="上映时间" prop="releaseTime">
      </el-table-column>
      <el-table-column align="center" label="时长(分钟)" prop="duration">
      </el-table-column>
      <el-table-column
        show-overflow-tooltip
        align="center"
        label="剧情简介"
        prop="synopsis"
      >
      </el-table-column>
      <el-table-column align="center" label="电影状态" prop="status">
        <template scope="scope">
          <el-button type="danger" disabled v-if="scope.row.status == 0"
            >正在热映</el-button
          >
          <el-button type="success" disabled v-if="scope.row.status == 1"
            >即将上映</el-button
          >
          <el-button type="info" disabled v-if="scope.row.status == 2"
            >已经下架</el-button
          >
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            circle
            @click="handleEdit(scope.$index, scope.row)"
          ></el-button>

          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
            @click="handleDelete(scope.$index, scope.row)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--    分页-->
    <template>
      <div class="block">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 15, 20, 25]"
          :page-size="5"
          layout="total, sizes, prev, pager, next, jumper"
          :total="count"
        >
        </el-pagination>
      </div>
    </template>
    <!--        修改对话框-->
    <el-dialog title="电影信息" :visible.sync="dialogFormVisible">
      <el-form :model="film">
        <el-form-item label="电影id" :label-width="formLabelWidth">
          <el-input v-model="film.id" :disabled="true"> </el-input>
        </el-form-item>
        <el-form-item label="海报地址" :label-width="formLabelWidth">
          <el-input v-model="film.poster" :disabled="true"> </el-input>
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
        <el-form-item label="票房" :label-width="formLabelWidth">
          <el-input v-model="film.boxOffice"> </el-input>
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
        <el-button @click="cancelSubmit">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import * as filmset from "@/api/filmset";
import * as dayjs from "dayjs";
export default {
  data() {
    return {
      tableData: [],
      search: "",
      currentPage: 1,
      size: 5,
      count: 0,
      //对话框
      dialogFormVisible: false,
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
      searchObj: {
        id: "",
        filmName: "",
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
    };
  },
  created() {
    this.getTotal();
    this.onSubmit();
  },
  methods: {
    getList() {
      filmset.getFilmList(this.currentPage, this.size).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data;
          console.log(res);
        }
      });
    },
    getTotal() {
      filmset.getFilmTotal().then((res) => {
        if (res.code == 200) {
          this.count = res.data;
        }
      });
    },
    // 分页
    handleSizeChange(val) {
      this.size = val; //每页显示数
      this.onSubmit();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.onSubmit();
    },
    //修改 数据回显
    handleEdit(index, row) {
      this.dialogFormVisible = true;
      let status = this.tableData[index].status; // 存储原始的 status 值
      this.film = this.tableData[index];
      if (status == 0) {
        this.film.status = "正在热映";
      } else if (status == 1) {
        this.film.status = "即将上映";
      } else if (status == 2) {
        this.film.status = "已经下架";
      }
      this.tableData[index].status = status; // 恢复原始的 status 值
    },
    submitForm() {
      this.film.releaseTime = dayjs(this.film.releaseTime).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      filmset.updateFilm(this.film).then((res) => {
        if (res.code == 200) {
          //修改成功
          this.$message.success("修改成功");
          this.getList();
          this.dialogFormVisible = false;
        }
        this.resetFilm();
      });
    },
    cancelSubmit() {
      this.resetFilm();
      this.dialogFormVisible = false;
    },
    //重置
    resetFilm() {
      this.film = {
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
      };
    },
    //删除数据
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除该电影, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        console.log(this);
        this.film = this.tableData[index];
        filmset.deleteFilm(this.film.id).then((res) => {
          if (res.code == 200) {
            //删除成功

            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.resetFilm();
            this.getTotal();
            this.getList();
          }
        });
      });
      // .catch(() => {
      //   this.$message({
      //     type: "info",
      //     message: "已取消删除",
      //   });
      // });
    },
    onSubmit() {
      console.log(this.searchObj);
      filmset
        .searchFilm(this.searchObj, this.currentPage, this.size)
        .then((res) => {
          if (res.code == 200) {
            this.tableData = res.data;
          }
        });
      filmset.getCountByCondition(this.searchObj).then((res) => {
        if (res.code == 200) {
          this.count = res.data;
          console.log(this.count);
        }
      });
    },
  },
};
</script>
