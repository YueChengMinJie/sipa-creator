<!--
 * @Author: caszhou
 * @Date: 2021-04-01 14:02:59
 * @Description: 修改用户的手机号
-->

<template>
  <div>
    <el-dialog
      title="修改账号"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="420px"
      @closed="handleClosed"
      @open="handleOpen"
    >
      <div v-loading="loading">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" @submit.native.prevent>
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="当前手机号:" prop="phoneNo">{{ formData.phoneNo }}</el-form-item>
          </div>
          <!-- form-row end -->
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="新手机号:" prop="newPhoneNo">
              <el-input v-model="formData.newPhoneNo" maxlength="16" placeholder="请输入新手机号"></el-input>
            </el-form-item>
          </div>
          <!-- form-row end -->
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="验证码:" prop="code">
              <el-input v-model="formData.code" maxlength="6" placeholder="请输入验证码"></el-input>
            </el-form-item>
            <el-button
              type="primary"
              v-if="codeNum === 0"
              class="app-ml10"
              @click="handleCodeSend()"
              :loading="codeLoading"
            >
              发送验证码
            </el-button>
            <el-button type="primary" v-else disabled class="app-ml10">倒计时{{ codeNum }}秒</el-button>
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
import { getPhoneCode, accountPhoneUpdate } from '@/api/sso-idp-server'

export default {
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      dialogVisible: false,
      formData: {
        phoneNo: '',
        newPhoneNo: '',
        code: ''
      },
      rules: {
        newPhoneNo: [
          { required: true, message: '请输入新手机号', trigger: 'blur' },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: '请输入合法的手机号',
            trigger: 'blur'
          }
        ],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      codeFun: null,
      codeNum: 0,
      codeLoading: false
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
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    }
  },
  methods: {
    // 打开
    handleOpen() {
      this.$nextTick(() => {
        this.formData.phoneNo = this.userInfo.phoneNo
      })
    },
    // 关闭dialog
    handleClosed() {
      clearInterval(this.codeFun)
      this.codeNum = 0
      this.$refs['formRef'].resetFields()
      this.$emit('input', false)
    },
    handleCancel() {
      this.dialogVisible = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await this.$refs['formRef'].validate()
        await accountPhoneUpdate({ phoneNo: this.formData.newPhoneNo, verificationCode: this.formData.code })
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
    },
    handleCodeSend() {
      this.$refs['formRef'].validateField('newPhoneNo', async valid => {
        if (!valid) {
          try {
            this.codeLoading = true
            await getPhoneCode({
              type: 'CHANGE_ACCOUNT',
              phoneNo: this.formData.newPhoneNo
            })
            this.$notify({
              title: '消息',
              message: '发送成功',
              type: 'success'
            })
            this.codeNum = 120
            this.codeFun = setInterval(() => {
              if (this.codeNum === 0) {
                clearInterval(this.codeFun)
                this.codeFun = null
              } else {
                this.codeNum--
              }
            }, 1000)
          } catch (error) {
            console.error('await:', error)
          } finally {
            this.codeLoading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped></style>
