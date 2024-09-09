<!--
 * @Author: caszhou
 * @Date: 2023-07-21 14:22:20
 * @Description: 角色 新增 / 编辑
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header"><el-page-header :content="pageTitle[pageType]" @back="goBack()" /></div>
    <el-form class="app-main" ref="formRef" :model="formData" :rules="formRules" label-width="100px">
      <div class="app-row">
        <el-form-item label="角色名称:" prop="name">
          <el-input v-model="formData.name" maxlength="20" placeholder="请输入角色名称" clearable />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="角色描述:" prop="remark">
          <el-input
            type="textarea"
            v-model="formData.remark"
            maxlength="60"
            rows="4"
            placeholder="请输入角色描述"
            resize="none"
            style="width: 400px"
          />
        </el-form-item>
      </div>
    </el-form>
    <div class="app-footer">
      <el-button @click="goBack">返 回</el-button>
      <el-button @click="handleConfirm" type="primary">确 认</el-button>
    </div>
  </div>
</template>

<script>
import { roleAdd, roleUpdate, roleSelectOne } from '@/api/sso-idp-server'

export default {
  data() {
    return {
      loading: false,
      pageTitle: { add: '新增', edit: '编辑' },
      pageType: 'add',
      pageId: '',
      formData: {
        name: '',
        remark: '',
        roleType: 2
      },
      formRules: {
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
      }
    }
  },
  async mounted() {
    try {
      this.loading = true
      const { type, id } = this.$route.query
      this.pageType = type
      this.pageId = id || ''
      if (this.pageType === 'edit') {
        await this.getDetail()
      }
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    async getDetail() {
      try {
        this.loading = true
        const { data } = await roleSelectOne({ id: this.pageId })
        this.formData.name = data.name
        this.formData.remark = data.remark
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    async handleConfirm() {
      try {
        this.loading = true
        await this.$refs['formRef'].validate
        if (this.pageType === 'add') {
          await roleAdd(this.formData)
          this.$notify({
            title: '消息',
            message: '新增成功',
            type: 'success'
          })
        } else {
          await roleUpdate({ id: this.pageId, ...this.formData })
          this.$notify({
            title: '消息',
            message: '编辑成功',
            type: 'success'
          })
        }
        this.goBack()
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped lang="scss"></style>
