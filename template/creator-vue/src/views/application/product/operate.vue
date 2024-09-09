<!--
 * @Author: caszhou
 * @Date: 2023-07-06 17:44:19
 * @Description: 新增 / 编辑
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header">
      <el-page-header :content="pageTitle[pageType]" @back="goBack()" />
    </div>
    <el-form class="app-main" ref="formRef" :model="formData" :rules="rules" label-width="140px">
      <el-divider content-position="left"><h3>基础信息</h3></el-divider>
      <div class="app-row">
        <el-form-item label="产品名称:" prop="name">
          <el-input v-model="formData.name" placeholder="请输入产品名称" clearable maxlength="10" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="产品描述:" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            placeholder="请输入产品描述"
            clearable
            :rows="4"
            maxlength="50"
            class="input400"
          />
        </el-form-item>
      </div>
      <el-divider content-position="left"><h3>资源信息</h3></el-divider>
      <div class="tree-box">
        <el-tree
          :data="treeData"
          show-checkbox
          node-key="id"
          :default-checked-keys="defaultCheckedKeys"
          :props="{ value: 'id', label: 'name', children: 'functionCodeVoList' }"
          ref="treeRef"
        ></el-tree>
      </div>
    </el-form>
    <div class="app-footer">
      <el-button @click="goBack">返回</el-button>
      <el-button @click="handleConfirm" type="primary">确认</el-button>
    </div>
  </div>
</template>

<script>
import { objectDeepCopy } from '@/utils/commonTools'
import { productAdd, productUpdate, productDetail, functionCodeList } from '@/api/sso-idp-server'

export default {
  data() {
    return {
      loading: false,
      pageType: 'add',
      pageAppId: '',
      pageTitle: {
        add: '新增',
        edit: '编辑'
      },
      formData: {
        applicationId: '',
        id: '',
        name: '',
        remark: ''
      },
      rules: {
        name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        remark: [{ required: true, message: '请输入产品描述', trigger: 'blur' }]
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
      this.formData.applicationId = appId
      await this.getFunctionCodeTree()
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
    async getDetail() {
      try {
        this.loading = true
        const { data } = await productDetail({ id: this.pageId })
        objectDeepCopy(this.formData, data)
        this.defaultCheckedKeys = this.getTreeEnabled(data.functionCodeVos)
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    async getFunctionCodeTree() {
      const { data } = await functionCodeList({ id: this.pageAppId })
      this.treeData = data
    },
    goBack() {
      this.$router.go(-1)
    },
    async handleConfirm() {
      try {
        this.loading = true
        await this.$refs['formRef'].validate()
        let message = ''
        // 获取选中和半选的所有key
        const functionCodeIds = [].concat(
          this.$refs['treeRef'].getCheckedKeys(),
          this.$refs['treeRef'].getHalfCheckedKeys()
        )
        const params = {
          ...this.formData,
          functionCodeIds
        }
        if (this.pageType === 'add') {
          await productAdd(params)
          message = '新增成功'
        } else {
          await productUpdate(params)
          message = '编辑成功'
        }
        this.goBack()
        this.$notify({
          title: '消息',
          message,
          type: 'success'
        })
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },

    getTreeEnabled(tree) {
      const result = []
      tree.forEach(item => {
        if (item.functionCodeVoList?.length > 0) {
          const childRes = this.getTreeEnabled(item.functionCodeVoList)
          if (childRes.length > 0) {
            result.push(...childRes)
          }
        } else {
          result.push(item.id)
        }
      })
      return result
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
