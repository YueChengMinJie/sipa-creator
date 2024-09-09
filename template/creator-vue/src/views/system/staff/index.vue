<!--
 * @Author: caszhou
 * @Date: 2023-07-19 15:41:28
 * @Description: 人员组织
-->

<template>
  <div v-loading="loading" class="app-composite">
    <unit-category ref="category" :tree-data="treeData" :shrink="ifShrink" @node-click="handleNodeClick" />
    <div class="app-container">
      <div class="app-shrink" @click="handleShrink">
        <i :class="ifShrink ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
        <span>{{ ifShrink ? '点击展开' : '点击收起' }}</span>
      </div>
      <div class="app-part">
        <div class="app-item">条件筛选:</div>
        <div class="app-item">
          <el-input
              placeholder="请输入手机号/姓名/工号"
              maxlength="50"
              v-model="search.queryLike"
              @keyup.native.enter="initList"
              clearable
          >
            <el-button slot="append" icon="el-icon-search" @click="initList"></el-button>
          </el-input>
        </div>
      </div>
      <div class="app-part">
        <div class="app-item">功能操作:</div>
        <div class="app-item">
          <el-button
              type="primary"
              icon="el-icon-plus"
              @click="handleOperate('add')"
              :disabled="disableAddUpdateDelete"
          >
            新增人员
          </el-button>
        </div>
        <div class="app-item">
          <el-popconfirm
              title="确认要批量删除这些数据吗?"
              placement="top"
              transition="el-zoom-in-left"
              @confirm="handleTableDelete(multipleSelection)"
          >
            <el-button
                type="danger"
                icon="el-icon-delete"
                slot="reference"
                :disabled="disableAddUpdateDelete || multipleSelection.length < 1"
            >
              批量删除
            </el-button>
          </el-popconfirm>
        </div>
      </div>
      <!-- 公共表格 -->
      <div v-loading="tableLoading" class="app-table">
        <el-table height="100%" :data="tableData" @selection-change="handleSelectionChange" row-key="id">
          <el-table-column type="selection" :selectable="ifAllowOperation" align="center" width="55" />
          <el-table-column label="序号" width="60" show-overflow-tooltip>
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column label="员工姓名" min-width="150" show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ scope.row.name || '--' }}</span>
              <el-tag v-if="scope.row.supperManager" type="warning" class="app-ml10">系统管理员</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="账号(手机号)" width="150">
            <template slot-scope="scope">
              <div class="custom-cellphone">
                <span>
                  {{ scope.row.phoneNo || '--' }}
                </span>
                <el-popconfirm
                    title="是否将该账号密码重置为: Aa123456 ?"
                    @confirm="handlePasswordReset(scope.row.id)"
                    transition="el-zoom-in-left"
                    v-if="ifAllowOperation(scope.row)"
                >
                  <el-link type="danger" :underline="false" slot="reference">重置密码</el-link>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="邮箱" width="200">
            <template slot-scope="scope">
              {{ scope.row.mail || '--' }}
            </template>
          </el-table-column>
          <el-table-column label="工号" width="130">
            <template slot-scope="scope">
              {{ scope.row.jobNumber || '--' }}
            </template>
          </el-table-column>
          <el-table-column label="所属部门" min-width="150" prop="departmentName" show-overflow-tooltip>
            <template slot-scope="scope">
              <div class="custom-style">
                <span v-if="scope.row.organizations.length > 0">
                  {{ scope.row.organizations[0].treeName }}
                </span>
                <el-popover
                    placement="top-start"
                    title="所属部门"
                    width="250"
                    trigger="hover"
                    transition="el-zoom-in-left"
                >
                  <div v-for="(item, index) in scope.row.organizations" :key="`departments-${index}`">
                    <el-tag class="app-mb10 app-ellipsis width100">
                      {{ item.treeName }}
                    </el-tag>
                  </div>
                  <el-tag v-show="scope.row.organizations.length > 1" class="app-ml10" slot="reference">
                    +{{ scope.row.organizations.length - 1 }}
                  </el-tag>
                </el-popover>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="启用状态" width="100">
            <template slot-scope="scope">
              <el-switch
                  v-if="ifAllowOperation(scope.row)"
                  v-model="scope.row.status"
                  active-color="#13ce66"
                  :inactive-value="0"
                  :active-value="1"
                  @change="handleEnabled(scope.row)"
              />
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column label="员工角色" min-width="150" prop="departmentName" show-overflow-tooltip>
            <template slot-scope="scope">
              <div class="custom-style">
                <span v-if="scope.row.roles.length > 0">{{ scope.row.roles[0].name }}</span>
                <span v-else>--</span>
                <el-popover
                    placement="top-start"
                    title="员工角色"
                    width="250"
                    trigger="hover"
                    transition="el-zoom-in-left"
                >
                  <div>
                    <el-tag
                        v-for="(item, index) in scope.row.roles"
                        :key="`roles-${index}`"
                        class="app-mr10 app-mb10 app-ellipsis width100"
                    >
                      {{ item.name.length > 16 ? `${item.name.substring(0, 16)}...` : item.name }}
                    </el-tag>
                  </div>
                  <el-tag v-show="scope.row.roles.length > 1" class="app-ml10" slot="reference">
                    +{{ scope.row.roles.length - 1 }}
                  </el-tag>
                </el-popover>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="120">
            <template slot-scope="scope" v-if="ifAllowOperation(scope.row)">
              <el-link
                  icon="el-icon-edit"
                  class="operate"
                  @click="handleOperate('edit', scope.row.id)"
                  :disabled="disableAddUpdateDelete"
              >
                编辑
              </el-link>
              <el-popconfirm
                  title="确认要删除这条数据吗?"
                  placement="top"
                  transition="el-zoom-in-left"
                  @confirm="handleTableDelete(scope.row.id)"
                  class="operate"
              >
                <el-link icon="el-icon-delete" slot="reference" :disabled="disableAddUpdateDelete">删除</el-link>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 公共表格 结束 -->
      <!-- 对应表格分页 -->
      <div class="app-pagination">
        <el-pagination
            background
            :current-page="pagination.current"
            :page-sizes="pagination.sizes"
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            :pager-count="pagination.pagerCount"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
      <!-- 对应表格分页 结束 -->
    </div>
  </div>
</template>

<script>
import MixinPage from '@/utils/mixinPage'
import UnitCategory from './UnitCategory'
import { orgGet, accountByOrgsGet, accountDelete, accountLock, accountPasswordReset } from '@/api/sso-idp-server'

export default {
  name: 'systemStaffIndex',
  components: {
    UnitCategory
  },
  mixins: [MixinPage],
  data() {
    return {
      listApi: accountByOrgsGet,
      deleteApi: accountDelete,
      tableLoading: false,
      search: {
        queryLike: '',
        orgId: ''
      },
      ifShrink: false,
      treeData: [],
      disableAddUpdateDelete: false
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    }
  },
  async activated() {
    try {
      this.loading = true
      await this.getTree()
      if (this.treeData.length > 0 && !this.search.orgId) {
        const orgId = this.treeData[0].id
        this.search.orgId = orgId
      }
      this.$refs['category'].handleSetCheck(this.search.orgId)
      await this.getList()
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    handleNodeClick(data) {
      this.search.orgId = data.id
      this.initList()
    },
    // 点击收缩
    handleShrink() {
      this.ifShrink = !this.ifShrink
    },
    async getTree() {
      const res = await orgGet()
      this.treeData = JSON.parse(JSON.stringify(res.data).replace(/,"children":\[\]/g, ''))
    },
    async initTree() {
      try {
        this.loading = true
        await this.getTree()
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async handleEnabled(data) {
      try {
        await accountLock({ id: data.id, status: data.status })
      } catch (error) {
        console.error('await:', error)
        data.status = data.status === 0 ? 1 : 0
      }
    },
    async handlePasswordReset(id) {
      try {
        this.loading = true
        await accountPasswordReset({ id })
        await this.getList()
        this.$notify({
          title: '消息',
          message: '重置成功',
          type: 'success'
        })
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    handleOperate(type, id) {
      this.$router.push({ name: 'systemStaffOperate', query: { type, id, pId: this.search.orgId } })
    },
    ifAllowOperation(data) {
      return !data.own && !data.supperManager
    }
  }
}
</script>

<style lang="scss" scoped>
.app-composite {
  .custom-style {
    display: flex;
    align-items: center;

    .el-tag {
      cursor: pointer;
    }
  }
  .custom-cellphone {
    line-height: 24px;
  }
}
.width100 {
  max-width: 100%;
}
</style>
