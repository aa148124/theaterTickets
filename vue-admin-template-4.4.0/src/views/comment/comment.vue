<template>
  <div style="margin: 50px">
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
      <el-table-column align="center" label="用户id" prop="userId">
      </el-table-column>
      <el-table-column align="center" label="电影名称" prop="filmName">
      </el-table-column>
      <el-table-column align="center" label="评分" prop="score">
      </el-table-column>
      <el-table-column align="center" label="评论内容" prop="content">
      </el-table-column>
      <el-table-column align="center" label="评论时间" prop="commentTime">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
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
  </div>
</template>

<script>
import * as filmset from "@/api/filmset";
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

      Comment: {
        id: "",
        username: "",
        filmName: "",
        content: "",
        score: "",
        commentTime: "",
      },
    };
  },
  created() {
    this.getTotal();
    this.getList();
  },
  methods: {
    getList() {
      filmset.getCommentList(this.currentPage, this.size).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data;
          console.log(res);
        }
      });
    },
    getTotal() {
      filmset.getCommentTotal().then((res) => {
        if (res.code == 200) {
          this.count = res.data;
          console.log(this.count);
        }
      });
    },
    // 分页
    handleSizeChange(val) {
      this.size = val; //每页显示数
      this.getList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getList();
    },
    //删除数据
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除该用户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.comment = this.tableData[index];
          filmset.deleteComment(this.comment.id).then((res) => {
            if (res.code == 200) {
              //删除成功

              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.resetComment();
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
  },
};
</script>
