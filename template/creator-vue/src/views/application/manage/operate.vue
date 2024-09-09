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
    <el-form class="app-main" ref="formRef" :model="formData" :rules="rules" label-width="120px">
      <div v-if="pageType === 'add'" class="app-row">
        <el-form-item label="应用ID:" prop="applicationId">
          <el-input
            v-model="formData.applicationId"
            placeholder="前台应用ID请手动设置小于1000101"
            clearable
            maxlength="10"
            class="input400"
          />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="应用名称:" prop="name">
          <el-input v-model="formData.name" placeholder="请输入内容" clearable maxlength="10" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="本地环境URL:" prop="localUrl">
          <el-input v-model="formData.localUrl" placeholder="请输入URL" clearable maxlength="50" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="测试环境URL:" prop="devUrl">
          <el-input v-model="formData.devUrl" placeholder="请输入URL" clearable maxlength="50" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="灰度环境URL:" prop="fatUrl">
          <el-input v-model="formData.fatUrl" placeholder="请输入URL" clearable maxlength="50" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="正式环境URL:" prop="url">
          <el-input v-model="formData.url" placeholder="请输入URL" clearable maxlength="50" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="应用描述:" prop="remark">
          <el-input
            type="textarea"
            v-model="formData.remark"
            placeholder="请输入应用描述"
            maxlength="50"
            class="input400"
          />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="创建默认服务:" prop="createDefaultService">
          <el-radio-group v-model="formData.createDefaultService" :disabled="pageType === 'edit'">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
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
import { applicationAdd, applicationUpdate, applicationById } from '@/api/sso-idp-server'

export default {
  data() {
    const ruleUrl = [
      { required: true, message: '请输入URL', trigger: 'blur' },
      { pattern: /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i, message: '请输入合法的URL', trigger: 'blur' }
    ]
    return {
      loading: false,
      pageType: 'add',
      pageTitle: {
        add: '新增',
        edit: '编辑'
      },
      formData: {
        applicationId: '',
        id: '',
        name: '',
        url: '',
        localUrl: '',
        devUrl: '',
        fatUrl: '',
        remark: '',
        createDefaultService: true
      },
      rules: {
        name: [{ required: true, message: '请输入应用名称', trigger: 'blur' }],
        url: ruleUrl,
        localUrl: ruleUrl,
        devUrl: ruleUrl,
        fatUrl: ruleUrl,
        applicationId: [{ pattern: /^\d+$/, message: '只能输入纯数字', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    const { id, type } = this.$route.query
    this.pageId = id
    this.pageType = type
    if (this.pageType === 'edit') {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      try {
        this.loading = true
        const { data } = await applicationById({ id: this.pageId })
        objectDeepCopy(this.formData, data)
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    async handleConfirm() {
      try {
        this.loading = true
        await this.$refs['formRef'].validate()
        let message = ''
        if (this.pageType === 'add') {
          await applicationAdd(this.formData)
          message = '新增成功'
        } else {
          delete this.formData.applicationId
          await applicationUpdate(this.formData)
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
    }
  }
}
</script>

<style scoped lang="scss">
.input400 {
  width: 400px;
}
</style>
