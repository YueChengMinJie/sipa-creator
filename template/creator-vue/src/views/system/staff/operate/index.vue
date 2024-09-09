<!--
 * @Author: caszhou
 * @Date: 2023-07-18 15:31:07
 * @Description: 人事 新增 / 编辑
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header">
      <el-page-header @back="goBack" :content="pageTitle[pageType]" />
    </div>
    <el-form
        class="app-main"
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        @submit.native.prevent
    >
      <el-form-item prop="id" v-show="false" />
      <el-form-item label="所属部门:" prop="departmentIds">
        <el-cascader
            v-model="formData.departmentIds"
            :options="treeData"
            :props="{
            checkStrictly: true,
            value: 'id',
            label: 'name',
            multiple: true,
            emitPath: false,
            children: 'childOrganizations'
          }"
            placeholder="请选择部门"
            @visible-change="handleCascaderStuck()"
        />
      </el-form-item>
      <div class="app-row">
        <el-form-item label="员工姓名:" prop="name">
          <el-input v-model="formData.name" placeholder="请输入员工姓名" maxlength="20" clearable></el-input>
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="手机号:" prop="cellphone">
          <el-input v-model="formData.cellphone" placeholder="请输入手机号" maxlength="11" clearable></el-input>
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="邮箱:" prop="mail">
          <el-input v-model="formData.mail" placeholder="请输入邮箱" maxlength="255" clearable></el-input>
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="工号:" prop="employeeNumber">
          <el-input v-model="formData.employeeNumber" placeholder="请输入工号" maxlength="10" clearable></el-input>
        </el-form-item>
      </div>
      <div class="app-row">
        <el-form-item label="关联角色:" prop="roles">
          <el-button type="primary" @click="handleRoleSelect()">选择角色</el-button>
          <el-table :data="formData.roles" class="app-table">
            <el-table-column label="序号" width="60" show-overflow-tooltip>
              <template slot-scope="scope">{{ scope.$index + 1 }}</template>
            </el-table-column>
            <el-table-column label="角色名称" width="100" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ scope.row.name || '--' }}
              </template>
            </el-table-column>
            <el-table-column label="角色描述" min-width="150" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ scope.row.remark || '--' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template slot-scope="scope">
                <el-link class="operate" icon="el-icon-delete" @click="handleDelete(scope.$index)">删除</el-link>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </div>
    </el-form>
    <div class="app-footer">
      <el-button @click="goBack">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 认</el-button>
    </div>
    <!-- 选择角色 -->
    <unit-role-select :show.sync="roleSelectVisible" :current-data="formData.roles" @confirm="handleRoleConfirm" />
  </div>
</template>

<script>
import UnitRoleSelect from './UnitRoleSelect'
import { accountAddOrUpdate, orgGet, accountSelect } from '@/api/sso-idp-server'
import { handleCascaderStuck } from '@/utils/commonTools'

export default {
  components: { UnitRoleSelect },
  data() {
    return {
      loading: false,
      pageTitle: { add: '新增', edit: '编辑' },
      pageType: 'add',
      pageId: '',
      formData: {
        id: '',
        name: '',
        cellphone: '',
        departmentIds: [],
        mail: '',
        employeeNumber: '',
        roles: []
      },
      formRules: {
        departmentIds: [{ required: true, message: '请选择所属部门', trigger: 'blur' }],
        name: [{ required: true, message: '请输入人员姓名', trigger: 'blur' }],
        cellphone: [
          { required: true, message: '请输入员工手机号', trigger: 'blur' },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: '请输入合法的手机号!',
            trigger: 'blur'
          }
        ],
        mail: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        employeeNumber: [{ required: true, message: '请输入工号', trigger: 'blur' }]
      },
      treeData: [],
      roleSelectVisible: false
    }
  },
  async mounted() {
    try {
      this.loading = true
      const { type, id, pId } = this.$route.query
      this.pageType = type
      this.pageId = id || ''
      if (pId) {
        this.formData.departmentIds.push(pId)
      }
      await this.getTree()
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
    goBack() {
      this.$router.go(-1)
    },
    handleDelete(index) {
      this.formData.roles.splice(index, 1)
    },
    handleRoleSelect() {
      this.roleSelectVisible = true
    },
    async getDetail() {
      const { data } = await accountSelect({ id: this.pageId })
      this.formData.id = data.id
      this.formData.name = data.name
      this.formData.cellphone = data.phoneNo
      this.formData.mail = data.mail
      this.formData.employeeNumber = data.jobNumber
      this.formData.departmentIds = data.organizations.map(item => item.id)
      this.formData.roles = data.roles || []
    },
    async getTree() {
      const res = await orgGet()
      this.treeData = JSON.parse(JSON.stringify(res.data).replace(/,"children":\[\]/g, ''))
    },
    async handleConfirm() {
      try {
        this.loading = true
        await this.$refs['formRef'].validate
        const params = {
          id: this.pageType === 'add' ? undefined : this.formData.id,
          name: this.formData.name,
          phoneNo: this.formData.cellphone,
          mail: this.formData.mail,
          jobNumber: this.formData.employeeNumber,
          orgIds: this.formData.departmentIds,
          roleIds: this.formData.roles.map(item => item.id)
        }
        await accountAddOrUpdate(params)
        this.$notify({
          title: '消息',
          message: this.pageType === 'add' ? '新增成功' : '编辑成功',
          type: 'success'
        })
        this.goBack()
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    handleRoleConfirm(data) {
      this.formData.roles.push(...data)
    }
  }
}
</script>

<style scoped lang="scss"></style>
