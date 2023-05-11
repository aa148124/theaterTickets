<template>
    <div id="app">
        <div>
            <el-card class="box-card" >
                <div slot="header" class="clearfix">
                    <span>修改密码</span>
                </div>
                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                         class="demo-ruleForm">
                    <el-form-item label="旧密码" prop="oldPass">
                        <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPass">
                        <el-input type="password" v-model="ruleForm.newPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="checkPass">
                        <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </div>
    </div>
</template>
<style>
.text {
    font-size: 14px;
}

.item {
    margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}
.clearfix:after {
    clear: both
}

.box-card {
    width: 480px;
}
#app{
    display:flex;
    justify-content: center;
    align-items: center;
}

</style>
<script>
import * as filmset from '@/api/filmset'
import { removeToken } from '@/utils/auth';

export default {

    data(){
        var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.newPass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                ruleForm: {
                    oldPass: '',
                    newPass: '',
                    checkPass: ''
                },
                rules: {
                    oldPass: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    newPass: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    checkPass: [
                        {validator: validatePass2, trigger: 'blur'}
                    ]

                },
                formData:{
                    token:'',
                    oldPassword: '',
                    newPassword: ''
                }
            };
        },
    created(){
        
    },
    methods:{
        submitForm(formName) {
            //获取token
            const token = document.cookie.split("=")[1];
            this.formData.token = token;
            this.formData.oldPassword = this.ruleForm.oldPass;
            this.formData.newPassword = this.ruleForm.newPass;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    filmset.changePwd(this.formData).then((res) => {
                        if (res.code == 200) {

                            this.$message.success('修改成功,请重新登录');
                            removeToken();
                            setTimeout(function(){window.location.href = "login";},2000);

                        } else {
                           this.$message.error(res.msg);
                        }
                    })
                } else {
                    return false;
                }
            });
        },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
    }

}
</script>
