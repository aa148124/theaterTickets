<template>
    <div style="margin: 50px;">
        <el-table
                :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
                style="width: 100%">
            <el-table-column
                    align="center"
                    label="id"
                    prop="id">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="用户名"
                    prop="username">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="电话"
                    prop="phone">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="昵称"
                    prop="nickName">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="操作">

                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-edit"
                            circle @click="handleEdit(scope.$index, scope.row)"></el-button>

                    <el-button type="danger" icon="el-icon-delete" circle
                            @click="handleDelete(scope.$index, scope.row)"></el-button>
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
                        :page-sizes="[5,10,15,20,25]"
                        :page-size="5"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="count">
                </el-pagination>
            </div>
        </template>
        <!--        修改对话框-->
        <el-dialog title="用户信息" :visible.sync="dialogFormVisible">
            <el-form :model="admin">
                <el-form-item label="用户id" :label-width="formLabelWidth">
                    <el-input
                            v-model="admin.id"
                            :disabled="true">
                    </el-input>
                </el-form-item>
                <el-form-item label="用户名" :label-width="formLabelWidth">
                    <el-input
                            v-model="admin.username"
                            :disabled="true">
                    </el-input>
                </el-form-item>
                <el-form-item label="手机号" :label-width="formLabelWidth">
                    <el-input
                            v-model="admin.phone">
                    </el-input>
                </el-form-item>
                <el-form-item label="昵称" :label-width="formLabelWidth">
                    <el-input
                            v-model="admin.nickName">
                    </el-input>
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
import * as filmset from '@/api/filmset'
export default{
    data() {
            return {
                tableData: [],
                search: '',
                currentPage: 1,
                size: 5,
                count: 0,

                //对话框
                dialogFormVisible: false,
                formLabelWidth: '100px',

                admin: {
                    id: '',
                    username: '',
                    phone: '',
                    nickName: '',
                },
            }
        },
    created(){
        this.getTotal();
        this.getList();
        
    },
    methods:{
        getList(){
            filmset.getAdminList(this.currentPage, this.size).then(res => {
                if(res.code == 200){
                    this.tableData = res.data;
                    console.log(res)
                }
            })
        },
        getTotal(){
            filmset.getAdminTotal().then(res => {
                if(res.code == 200){
                    
                    this.count = res.data;
                    console.log(this.count)
                }
            })
        },
        // 分页
        handleSizeChange(val) {
            this.size = val;//每页显示数
            this.getList();

        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.getList();
        },
        //修改 数据回显
        handleEdit(index, row) {
            this.dialogFormVisible = true;
            this.admin = this.tableData[index];
        },
        submitForm() {
            filmset.updateAdmin(this.admin).then(res => {
                if (res.code == 200) {
                    //修改成功
                    this.$message.success("修改成功")
                    this.getList();
                    this.dialogFormVisible = false;
                }
                this.resetAdmin();
            })
        },
        cancelSubmit(){
            this.resetAdmin();
            this.dialogFormVisible = false;
        },
        //重置
        resetAdmin() {
            this.user = {
                    id: '',
                    username: '',
                    phone: '',
                    nickName: ''
            }
        },
        //删除数据
        handleDelete(index, row) {
            this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.admin = this.tableData[index];
                filmset.deleteAdmin(this.admin.id).then(res => {
                    if (res.code == 200) {
                        //删除成功

                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.resetAdmin();
                        this.getTotal();
                        this.getList();
                    }
                })


            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });

        },

    }
}
</script>