<!--
 * @Author: caszhou
 * @Date: 2021-04-01 14:02:59
 * @Description: 修改账号的密码
-->

<template>
  <div>
    <el-dialog
      title="修改密码"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="385px"
      @open="handleOpen()"
      @closed="handleClosed()"
    >
      <div v-loading="loading">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" @submit.native.prevent>
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="当前密码:" prop="pwd">
              <el-input
                v-model="formData.pwd"
                maxlength="15"
                type="password"
                placeholder="请输入当前密码"
                show-password
              />
            </el-form-item>
          </div>
          <!-- form-row end -->
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="新密码:" prop="newPwd">
              <el-input
                v-model="formData.newPwd"
                maxlength="15"
                type="password"
                placeholder="请输入新密码"
                show-password
              ></el-input>
            </el-form-item>
          </div>
          <!-- form-row end -->
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="重复密码:" prop="newPwdR">
              <el-input
                v-model="formData.newPwdR"
                maxlength="15"
                type="password"
                placeholder="请重复输入新密码"
                show-password
              ></el-input>
            </el-form-item>
          </div>
          <!-- form-row end -->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleCancel()">取 消</el-button>
          <el-button type="primary" @click="handleConfirm">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { passwordUpdate } from '@/api/sso-idp-server'

export default {
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validatePass = (_rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.formData.newPwd) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      dialogVisible: false,
      formData: {
        pwd: '',
        newPwd: '',
        newPwdR: ''
      },
      rules: {
        pwd: [{ required: true, message: '当前密码不能为空', trigger: 'blur' }],
        newPwd: [{ required: true, message: '新密码不能为空', trigger: 'blur' }],
        newPwdR: [
          { required: true, message: '重复密码不能为空', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    value: {
      immediate: true,
      handler: function (val) {
        this.dialogVisible = val
      }
    }
  },
  methods: {
    // 关闭dialog
    handleClosed() {
      this.$refs['formRef'].resetFields()
      this.$emit('input', false)
    },
    // 打开
    handleOpen() {},
    handleCancel() {
      this.dialogVisible = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await this.$refs['formRef'].validate()
        await passwordUpdate({ credential: this.formData.newPwd, oldCredential: this.formData.pwd })
        await this.$store.dispatch('updateUserInfo')
        this.handleCancel()
        this.$notify({
          title: '消息',
          message: '修改成功',
          type: 'success'
        })
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
      // this.$refs['formRef'].validate(valid => {
      //   if (valid) {
      //     this.loading = true
      //     this.submitEditor()
      //   }
      // })
    }
  }
}
</script>

<style lang="scss" scoped></style>
