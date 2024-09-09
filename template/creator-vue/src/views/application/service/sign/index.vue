<!--
 * @Author: caszhou
 * @Date: 2023-08-14 15:23:10
 * @Description: 签约管理
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header">
      <el-page-header content="签约管理" @back="goBack()" />
    </div>
    <div class="app-part">
      <div class="app-item">功能操作:</div>
      <div class="app-item">
        <el-button type="primary" icon="el-icon-check" @click="handleSign()">签约商户</el-button>
      </div>
    </div>
    <!-- 公共表格 -->
    <div class="app-table">
      <el-table height="100%" :data="tableData">
        <el-table-column label="商户编号" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.serialNo || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="商户账号" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.adminPhone || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="商户简称" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.name || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="150">
          <template slot-scope="scope">
            {{ dateTimeStr(scope.row.createTime) }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="操作" fixed="right" width="80">
          <template slot-scope="scope">
            <el-popconfirm
              title="你确认进行解约操作？"
              placement="top"
              transition="el-zoom-in-left"
              @confirm="handleSecure(scope.row.id)"
            >
              <el-link slot="reference" icon="el-icon-close" class="operate">解约</el-link>
            </el-popconfirm>
          </template>
        </el-table-column> -->
      </el-table>
    </div>
    <!-- 公共表格 结束 -->
    <!-- 签约商户 -->
    <unit-operate v-model="operateDialogVisible" :id="operateDialogId" />
  </div>
</template>

<script>
import MixinPage from '@/utils/mixinPage'
import UnitOperate from './UnitOperate.vue'
import { dateTimeStr } from '@/utils/commonTools'
import { serviceCompanySigningList } from '@/api/sso-idp-server'

export default {
  mixins: [MixinPage],
  components: { UnitOperate },
  data() {
    return {
      listApi: serviceCompanySigningList,
      // deleteApi: () => Promise
      search: { id: '' },
      operateDialogVisible: false,
      operateDialogId: ''
    }
  },
  mounted() {
    this.search.id = this.$route.query.id
    this.initList()
  },
  methods: {
    dateTimeStr,
    goBack() {
      this.$router.go(-1)
    },
    handleSign() {
      this.operateDialogId = this.search.id
      this.operateDialogVisible = true
    },
    async handleSecure() {
      try {
        this.loading = true
        // await xx()
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
