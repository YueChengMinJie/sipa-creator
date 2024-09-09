<!--
 * @Author: caszhou
 * @Date: 2023-07-21 14:22:20
 * @Description: 角色查看
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header"><el-page-header content="角色查看" @back="goBack()" /></div>
    <el-form class="app-main" ref="formRef" :model="formData" label-width="100px">
      <div class="app-row">
        <el-form-item label="角色名称:" prop="name">
          {{ formData.name }}
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item class="app-col" label="角色描述:" prop="remark">
          {{ formData.remark || '--' }}
        </el-form-item>
      </div>
    </el-form>
    <div class="app-footer">
      <el-button @click="goBack">返 回</el-button>
    </div>
  </div>
</template>

<script>
import { roleSelectOne } from '@/api/sso-idp-server'

export default {
  data() {
    return {
      loading: false,
      pageId: '',
      formData: {
        name: '',
        remark: ''
      }
    }
  },
  async mounted() {
    try {
      this.loading = true
      const { id } = this.$route.query
      this.pageId = id || ''
      await this.getDetail()
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
    }
  }
}
</script>

<style scoped lang="scss"></style>
