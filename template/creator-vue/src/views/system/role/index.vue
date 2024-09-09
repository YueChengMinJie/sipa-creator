<!--
 * @Author: caszhou
 * @Date: 2023-07-19 15:54:53
 * @Description: 角色权限
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-part">
      <div class="app-item">条件筛选:</div>
      <div class="app-item">
        <el-input
          placeholder="请输入角色名称"
          maxlength="32"
          v-model="search.queryLike"
          @keyup.native.enter="initList"
          clearable
        >
          <el-button slot="append" icon="el-icon-search" @click="initList" />
        </el-input>
      </div>
    </div>
    <div class="app-part">
      <div class="app-item">功能操作:</div>
      <div class="app-item">
        <el-button type="primary" icon="el-icon-plus" @click="handleOperate('add')">新增</el-button>
      </div>
    </div>
    <!-- 公共表格 -->
    <div class="app-table">
      <el-table height="100%" :data="tableData">
        <el-table-column label="序号" width="60" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column label="角色名称" min-width="170" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link @click="handleView(scope.row.id)" type="primary">
              {{ scope.row.name || '--' }}
            </el-link>
            <el-tag v-if="scope.row.acquiescence" type="warning" class="app-ml10">系统角色</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="角色描述" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.remark || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="启用状态" min-width="100" show-overflow-tooltip>
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
        <el-table-column label="创建人" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.createName || '--' }}</template>
        </el-table-column>
        <el-table-column label="创建时间" width="150">
          <template slot-scope="scope">
            {{ filterDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="修改人" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.updateName || '--' }}</template>
        </el-table-column>
        <el-table-column label="修改时间" width="150">
          <template slot-scope="scope">
            {{ filterDateTime(scope.row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template slot-scope="scope" v-if="ifAllowOperation(scope.row)">
            <el-link icon="el-icon-edit" class="operate" @click="handleOperate('edit', scope.row.id)">编辑</el-link>
            <el-link icon="el-icon-setting" class="operate" @click="handleAuthority(scope.row.id)">权限配置</el-link>
            <el-popconfirm
              title="确认要删除这条数据吗？"
              placement="top"
              transition="el-zoom-in-left"
              @confirm="handleTableDelete(scope.row.id)"
            >
              <el-link slot="reference" icon="el-icon-delete" class="operate">删除</el-link>
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
      />
    </div>
    <!-- 对应表格分页 结束 -->
  </div>
</template>

<script>
import MixinPage from '@/utils/mixinPage'
import { dateTimeStr } from '@/utils/commonTools'
import { roleAccountSelectByCompany, roleAccessDelete, roleLock } from '@/api/sso-idp-server'

export default {
  name: 'systemRoleIndex',
  mixins: [MixinPage],
  data() {
    return {
      listApi: roleAccountSelectByCompany,
      deleteApi: roleAccessDelete,
      search: { queryLike: '' }
    }
  },
  async activated() {
    try {
      this.loading = true
      await this.getList()
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    handleOperate(type, id) {
      this.$router.push({ name: 'systemRoleOperate', query: { type, id } })
    },
    handleView(id) {
      this.$router.push({ name: 'systemRoleView', query: { id } })
    },
    handleDateTimeChange() {
      if (this.search.dateTime) {
        this.search.startCreateTime = this.search.dateTime[0]
        this.search.endCreateTime = this.search.dateTime[1]
      } else {
        this.search.startCreateTime = ''
        this.search.endCreateTime = ''
      }
      this.initList()
    },
    filterDateTime(date) {
      return date ? dateTimeStr(date) : '--'
    },
    async handleEnabled(data) {
      try {
        await roleLock({ id: data.id, status: data.status })
      } catch (error) {
        console.error('await:', error)
        data.status = data.status === 0 ? 1 : 0
      }
    },
    ifAllowOperation(data) {
      return !data.own && !data.acquiescence
    },
    handleAuthority(id) {
      this.$router.push({ name: 'systemRoleAuthority', query: { id } })
    }
  }
}
</script>

<style scoped lang="scss"></style>
