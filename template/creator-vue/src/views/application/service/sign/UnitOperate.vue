<!--
 * @Author: caszhou
 * @Date: 2023-07-06 17:14:35
 * @Description: 签约商户
-->

<template>
  <div class="temp-universal-operate">
    <el-dialog
      title="签约商户"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="420px"
      @open="handleOpen()"
      @closed="handleClosed()"
    >
      <div class="dialog-container" v-loading="loading">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" @submit.native.prevent>
          <div class="app-row">
            <el-form-item label="商户编号:" prop="account">
              <el-input
                v-model="formData.account"
                type="textarea"
                :rows="4"
                maxlength="1000"
                placeholder="请输入编号，多个编号时可以换行"
                clearable
                style="width: 260px"
              />
            </el-form-item>
          </div>
          <div class="dialog-footer">
            <el-button @click="handleCancel()">取 消</el-button>
            <el-button type="primary" @click="handleConfirm">确 认</el-button>
          </div>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { serviceSigning } from '@/api/sso-idp-server'

export default {
  props: {
    value: {
      type: Boolean,
      default: false
    },
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      dialogVisible: false,
      formData: {
        account: ''
      },
      rules: {
        account: [
          { required: true, message: '请输入商户编号', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9]+$/, message: '只允许输入字母/数字', trigger: 'blur' }
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
    /** 关闭后的dialog */
    handleClosed() {
      this.$emit('input', false)
      this.$refs['formRef']?.resetFields()
    },
    /** 打开弹窗事件 */
    async handleOpen() {},
    handleCancel() {
      this.dialogVisible = false
    },
    /** 确认事件 */
    handleConfirm() {
      this.$refs['formRef'].validate(async valid => {
        if (valid) {
          try {
            const serialNo = this.formData.account.split('\n')
            const regular = /^[a-zA-Z0-9]+$/
            const ifQualified = serialNo.every(item => regular.test(item))
            if (!ifQualified) {
              this.$message.warning('请输入合法的格式或编号')
              return
            }
            this.loading = true
            await serviceSigning({ serviceId: this.id, serialNo })
            await this.$parent.getList()
            this.handleCancel()
            this.$notify({
              title: '消息',
              message: '设置成功',
              type: 'success'
            })
          } catch (error) {
            console.error('await:', error)
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>
