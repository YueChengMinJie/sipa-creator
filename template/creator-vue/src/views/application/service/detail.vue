<!--
 * @Author: caszhou
 * @Date: 2023-07-06 17:44:19
 * @Description: 查看
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header">
      <el-page-header content="查看详情" @back="goBack()" />
    </div>
    <el-form class="app-main" ref="formRef" :model="formData" label-width="130px">
      <el-divider content-position="left"><h3>基础信息</h3></el-divider>
      <div class="app-row">
        <el-form-item label="服务名称:" prop="name">
          {{ formData.name }}
        </el-form-item>
      </div>
      <div class="app-row" style="margin-top: -10px">
        <el-form-item label="服务描述:" prop="remark">
          {{ formData.remark || '--' }}
        </el-form-item>
      </div>
      <el-divider content-position="left"><h3>产品信息</h3></el-divider>
      <div class="tree-box">
        <el-tree
          :data="treeData"
          show-checkbox
          node-key="id"
          :default-checked-keys="defaultCheckedKeys"
          :props="{ value: 'id', label: 'name', children: 'functionCodeVoList', disabled: () => true }"
          ref="treeRef"
        ></el-tree>
      </div>
    </el-form>
    <div class="app-footer">
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script>
import { objectDeepCopy } from '@/utils/commonTools'
import { serviceDetail, productList } from '@/api/sso-idp-server'

export default {
  data() {
    return {
      loading: false,
      pageAppId: '',
      formData: {
        id: '',
        name: '',
        remark: ''
      },
      treeData: [],
      defaultCheckedKeys: []
    }
  },
  async mounted() {
    try {
      this.loading = true
      const { id, type, appId } = this.$route.query
      this.pageId = id
      this.pageType = type
      this.pageAppId = appId
      await Promise.all([this.getFunctionCodeTree(), this.getDetail()])
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    async getDetail() {
      try {
        this.loading = true
        const { data } = await serviceDetail({ id: this.pageId })
        objectDeepCopy(this.formData, data)
        this.defaultCheckedKeys = data.productVos.map(item => item.id)
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    async getFunctionCodeTree() {
      const { data } = await productList({ id: this.pageAppId })
      this.treeData = data
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped lang="scss">
.input400 {
  width: 400px;
}
.tree-box {
  padding-left: 50px;
}
</style>
