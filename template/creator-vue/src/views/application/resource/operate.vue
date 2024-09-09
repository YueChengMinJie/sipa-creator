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
      <div class="app-row">
        <el-form-item label="资源类型:" prop="type">
          <el-radio-group v-model="formData.type" :disabled="pageType === 'edit'">
            <el-radio v-for="item in typeOptions" :key="item[0]" :label="item[0]">{{ item[1] }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
      <div v-if="formData.type === 'menu'" class="app-row">
        <el-form-item label="父菜单:" prop="parentId">
          <el-cascader
            ref="parentCascaderRef"
            v-model="formData.parentId"
            :options="treeData"
            :props="{
              checkStrictly: true,
              label: 'name',
              value: 'id',
              children: 'functionCodeVoList',
              emitPath: false
            }"
            clearable
            placeholder="请选择父菜单"
            class="input400"
            @visible-change="handleCascaderStuck()"
            @change="handleChangeParentId"
          />
        </el-form-item>
      </div>
      <div v-else class="app-row">
        <el-form-item label="父菜单:" prop="otherParentId">
          <el-cascader
            ref="parentOtherCascaderRef"
            v-model="formData.otherParentId"
            :options="treeData"
            :props="{
              checkStrictly: true,
              label: 'name',
              value: 'id',
              children: 'functionCodeVoList',
              emitPath: false
            }"
            clearable
            placeholder="请选择父菜单"
            class="input400"
            @visible-change="handleCascaderStuck()"
            @change="handleChangeOtherParentId"
          />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="资源中文:" prop="name">
          <el-input v-model="formData.name" placeholder="请输入资源中文" clearable maxlength="64" class="input400" />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="资源英文:" prop="aliasName">
          <el-input
            v-model="formData.aliasName"
            placeholder="请输入资源英文"
            clearable
            maxlength="100"
            class="input400"
          />
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="URL:" prop="url">
          <el-input v-model="formData.url" placeholder="请输入URL" clearable maxlength="50" class="input400" />
        </el-form-item>
      </div>
      <template v-if="['module', 'menu'].includes(formData.type)">
        <div class="app-row">
          <el-form-item label="HTML模板路径:" prop="componentName">
            <el-input
              v-model="formData.componentName"
              placeholder="请输入HTML模板路径"
              clearable
              maxlength="100"
              class="input400"
            />
          </el-form-item>
        </div>
        <div class="app-row">
          <el-form-item label="是否缓存:" prop="otherMeta.keepAlive">
            <el-radio-group v-model="formData.otherMeta.keepAlive">
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <div class="app-row">
          <el-form-item label="是否隐藏:" prop="hidden">
            <el-radio-group v-model="formData.hidden">
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <div class="app-row">
          <el-form-item label="重定向页面:" prop="redirectName">
            <el-cascader
              v-model="formData.redirectName"
              :options="treeData"
              :props="{
                checkStrictly: true,
                label: 'name',
                value: 'aliasName',
                children: 'functionCodeVoList',
                emitPath: false
              }"
              clearable
              placeholder="请选择重定向页面"
              class="input400"
              @visible-change="handleCascaderStuck()"
            />
          </el-form-item>
        </div>
        <div class="app-row">
          <el-form-item label="高亮菜单:" prop="highlightName">
            <el-cascader
              v-model="formData.highlightName"
              :options="treeData"
              :props="{
                checkStrictly: true,
                label: 'name',
                value: 'aliasName',
                children: 'functionCodeVoList',
                emitPath: false
              }"
              clearable
              placeholder="请选择高亮菜单"
              class="input400"
              @visible-change="handleCascaderStuck()"
            />
          </el-form-item>
        </div>
        <div class="app-row">
          <el-form-item label="图标:" prop="icon">
            <el-input v-model="formData.icon" placeholder="请输入图标" clearable maxlength="50" />
          </el-form-item>
        </div>
      </template>
    </el-form>
    <div class="app-footer">
      <el-button @click="goBack">返回</el-button>
      <el-button @click="handleConfirm" type="primary">确认</el-button>
    </div>
  </div>
</template>

<script>
import { objectDeepCopy, handleCascaderStuck } from '@/utils/commonTools'
import { functionCodeAdd, functionCodeUpdate, functionCodeById, functionCodeList } from '@/api/sso-idp-server'

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
        type: 'module',
        parentId: '',
        otherParentId: '',
        name: '',
        aliasName: '',
        highlightName: '',
        url: '',
        componentName: '',
        otherMeta: {
          keepAlive: false
        },
        hidden: false,
        redirectName: '',
        icon: '',
        ordinal: 0
      },
      rules: {
        name: [
          { required: true, message: '请输入资源中文', trigger: 'blur' },
          { pattern: /^[\u4e00-\u9fa5a-zA-Z]+$/, message: '资源中文只能包含中文或字母', trigger: 'blur' }
        ],
        aliasName: [
          { required: true, message: '请输入资源英文', trigger: 'blur' },
          { pattern: /^[a-zA-Z]+$/, message: '资源英文只能包含纯字母', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入URL', trigger: 'blur' },
          { pattern: /^[a-zA-Z/]+$/, message: 'URL只能包含纯字母或/', trigger: 'blur' }
        ],
        componentName: [
          { required: true, message: '请输入HTML模板路径', trigger: 'blur' },
          { pattern: /^[a-zA-Z/]+$/, message: 'HTML模板路径只能包含纯字母或/', trigger: 'blur' }
        ],
        parentId: [{ required: true, message: '请选择父菜单', trigger: 'change' }],
        icon: [{ pattern: /^[a-zA-Z-]+$/, message: '图标只能包含纯字母或-', trigger: 'blur' }]
      },
      typeOptions: new Map([
        ['module', '模块'],
        ['menu', '菜单'],
        ['page', '页面'],
        ['endpoint', '按钮']
      ]),
      treeData: []
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
      this.formData.ordinal = this.treeData.length
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
    handleCascaderStuck,
    async getDetail() {
      try {
        this.loading = true
        const { data } = await functionCodeById({ id: this.pageId })
        objectDeepCopy(this.formData, data)
        this.formData.otherParentId = data.parentId
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    async getFunctionCodeTree() {
      const { data } = await functionCodeList({ id: this.pageAppId })
      this.treeData = this.filterCurrentData(data)
    },
    filterCurrentData(data) {
      const tree = []
      data.forEach(item => {
        if (item.id !== this.pageId) {
          let functionCodeVoList = undefined
          if (item.functionCodeVoList && item.functionCodeVoList.length > 0) {
            functionCodeVoList = this.filterCurrentData(item.functionCodeVoList)
          }
          tree.push({
            ...item,
            functionCodeVoList
          })
        }
      })
      return tree
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
          await functionCodeAdd(this.formData)
          message = '新增成功'
        } else {
          await functionCodeUpdate(this.formData)
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
    handleChangeParentId(data) {
      this.formData.otherParentId = data
      const nodes = this.$refs['parentCascaderRef'].getCheckedNodes()
      if (nodes.length > 0) {
        const voList = nodes[0].data.functionCodeVoList
        this.formData.ordinal = voList ? voList.length : 0
      }
    },
    handleChangeOtherParentId(data) {
      this.formData.parentId = data
      const nodes = this.$refs['parentOtherCascaderRef'].getCheckedNodes()
      if (nodes.length > 0) {
        const voList = nodes[0].data.functionCodeVoList
        this.formData.ordinal = voList ? voList.length : 0
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
