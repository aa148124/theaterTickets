<template>
  <div style="margin: 50px">
    <el-button type="primary" round @click="dialogVisible = true"
      >新增排片</el-button
    >
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
      <el-table-column align="center" label="电影名称" prop="film.filmName">
      </el-table-column>
      <el-table-column align="center" label="影厅" prop="room.roomName">
      </el-table-column>
      <el-table-column align="center" label="放映时间" prop="playTime">
      </el-table-column>
      <el-table-column align="center" label="票价" prop="price">
      </el-table-column>
      <el-table-column align="center" label="余票" prop="stock">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <!--           <el-button
            type="primary"
            icon="el-icon-edit"
            circle
            @click="handleEdit(scope.$index, scope.row)"
          ></el-button>
 -->
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

    <!--        新增对话框-->
    <el-dialog title="新增排片" :visible.sync="dialogVisible" width="30%">
      <span>
        <el-form :model="arrange" label-width="100px" class="demo-ruleForm">
          <el-form-item label="电影Id" prop="filmName">
            <el-input v-model="arrange.filmId"></el-input>
          </el-form-item>
          <el-form-item label="影厅" prop="room">
            <el-select v-model="arrange.roomId" placeholder="请选择影厅">
              <el-option label="1号厅" value="1"></el-option>
              <el-option label="2号厅" value="2"></el-option>
              <el-option label="3号厅" value="3"></el-option>
              <el-option label="4号厅" value="4"></el-option>
              <el-option label="5号厅" value="5"></el-option>
              <el-option label="6号厅" value="6"></el-option>
              <el-option label="7号厅" value="7"></el-option>
              <el-option label="8号厅" value="8"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="放映时间">
            <div class="block">
              <el-date-picker
                v-model="arrange.playTime"
                type="datetime"
                placeholder="选择放映时间"
              >
              </el-date-picker>
            </div>
          </el-form-item>

          <el-form-item label="售价" prop="price">
            <el-input v-model="arrange.price"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">排片</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import * as filmset from "@/api/filmset";
import * as dayjs from "dayjs";
export default {
  data() {
    return {
      search: "",
      tableData: [],
      currentPage: 1,
      size: 5,
      count: 0,
      //对话框
      dialogVisible: false,
      formLabelWidth: "100px",
      arrange: {
        id: "",
        filmId: "", //电影id
        roomId: "", //影厅
        playTime: "", //放映时间
        price: "", //票价
      },
    };
  },
  created() {
    this.getTotal();
    this.getList();
  },
  methods: {
    getList() {
      filmset.getArrangeList(this.currentPage, this.size).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data;
          console.log(res);
        }
      });
    },
    getTotal() {
      filmset.getArrangeTotal().then((res) => {
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
      console.log(123);
      this.film = this.row;
    },
    submitForm() {
      this.arrange.playTime = dayjs(this.arrange.playTime).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      filmset
        .addArrange(this.arrange)
        .then((res) => {
          if (res.code == 200) {
            //修改成功
            this.$message.success("添加成功");
            this.dialogVisible = false;
            this.getList();
          } else {
            this.$message.console.error(res.msg);
          }
          this.resetArrange();
        })
        .catch((e) => {
          console.log(res);
        });
    },
    cancelSubmit() {
      this.resetArrange();
      this.dialogFormVisible = false;
    },
    //重置
    resetArrange() {
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
      this.$confirm("此操作将永久删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.film = this.tableData[index];
          filmset.deleteArrange(this.film.id).then((res) => {
            if (res.code == 200) {
              //删除成功

              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.resetArrange();
              this.getTotal();
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    onSubmit() {
      console.log(this.searchObj);
      filmset
        .searchArrange(this.searchObj, this.currentPage, this.size)
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
    resetForm() {
      this.arrange = {
        id: "",
        filmName: "", //电影名称
        room: "", //影厅
        playTime: "", //放映时间
        price: "", //票价
      };
    },
  },
};
</script>
