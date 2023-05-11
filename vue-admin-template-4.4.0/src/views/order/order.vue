<template>
  <div style="margin: 50px">
    <!--搜索表单-->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline">
      <el-form-item label="订单号">
        <el-input v-model="searchObj.id" placeholder="订单号"></el-input>
      </el-form-item>
      <el-form-item label="用户ID">
        <el-input v-model="searchObj.userId" placeholder="用户ID"></el-input>
      </el-form-item>
      <el-form-item label="场次ID">
        <el-input v-model="searchObj.arrangeId" placeholder="场次ID"></el-input>
      </el-form-item>
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
      <el-table-column align="center" label="订单号" prop="id">
      </el-table-column>
      <el-table-column align="center" label="用户id" prop="userId">
      </el-table-column>
      <el-table-column align="center" label="场次id" prop="arrangeId">
      </el-table-column>
      <el-table-column align="center" label="座位" prop="seat">
      </el-table-column>
      <el-table-column align="center" label="下单时间" prop="orderTime">
      </el-table-column>
      <el-table-column align="center" label="订单状态" prop="status">
        <template scope="scope">
          <el-button type="danger" disabled v-if="scope.row.status == 1"
            >未支付</el-button
          >
          <el-button type="success" disabled v-if="scope.row.status == 2"
            >已支付</el-button
          >
          <el-button type="info" disabled v-if="scope.row.status == 0"
            >已取消</el-button
          >
        </template>
      </el-table-column>
      <el-table-column align="center" label="金额" prop="price">
      </el-table-column>
      <el-table-column align="center" label="支付时间" prop="paymentTime">
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
      //搜索内容
      searchObj: {
        id: "",
        userId: "",
        arrangeId: "",
      },
    };
  },
  created() {
    this.getTotal();
    this.getList();
  },
  methods: {
    getList() {
      filmset.getOrderList(this.currentPage, this.size).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data;
          console.log(res);
        }
      });
    },
    getTotal() {
      filmset.getOrderTotal().then((res) => {
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
          this.order = this.tableData[index];
          filmset.deleteOrder(this.order.id).then((res) => {
            if (res.code == 200) {
              //删除成功

              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.resetOrder();
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
      filmset
        .searchOrder(this.searchObj, this.currentPage, this.size)
        .then((res) => {
          if (res.code == 200) {
            this.tableData = res.data;
          }
        });
    },
  },
};
</script>
