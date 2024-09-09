<!--
 * @Author: caszhou
 * @Date: 2023-08-11 17:02:21
 * @Description: 服务管理
-->
<template>
  <div class="app-container" v-loading="loading">
    <el-tabs v-model="activeName">
      <el-tab-pane v-for="(item, index) in appList" :key="item.id" :label="item.name" :name="`${index}`"></el-tab-pane>
    </el-tabs>
    <div class="app-main" v-loading="mainLoading">
      <div class="el-card card-add" @click="handleOperate('add')">
        <i class="el-icon-plus"></i>
        <span>新增服务</span>
      </div>
      <el-card v-for="item in tableData" :key="item.id">
        <div class="title app-ellipsis">
          <el-link @click="handleDetail(item.id)">{{ item.name || '--' }}</el-link>
        </div>
        <div class="intro">
          {{ item.remark || '--' }}
        </div>
        <div class="button-group">
          <div class="button">
            <el-link icon="el-icon-edit" @click="handleOperate('edit', item.id)">编辑</el-link>
          </div>
          <div class="line"></div>
          <div class="button">
            <el-link icon="el-icon-check" @click="handleSign(item.id)">签约</el-link>
          </div>
          <div class="line"></div>
          <div class="button">
            <el-popconfirm
              title="你确认进行删除操作？"
              placement="top"
              transition="el-zoom-in-left"
              @confirm="handleTableDelete(item.id, true)"
              :disabled="item.isAcquiescence"
            >
              <el-link slot="reference" icon="el-icon-delete" class="operate" :disabled="item.isAcquiescence">
                删除
              </el-link>
            </el-popconfirm>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import MixinPage from '@/utils/mixinPage'
import { applicationAll, serviceList, serviceDelete } from '@/api/sso-idp-server'

export default {
  name: 'applicationServiceIndex',
  mixins: [MixinPage],
  data() {
    return {
      listApi: serviceList,
      deleteApi: serviceDelete,
      deleteKey: 'id',
      activeName: '0',
      appList: [],
      search: { id: '' },
      openWatch: false,
      mainLoading: false
    }
  },
  watch: {
    async activeName() {
      if (this.openWatch) {
        this.search.id = this.appList[this.activeName].id
        try {
          this.mainLoading = true
          await this.getCardData()
        } catch (error) {
          console.error('await:', error)
        } finally {
          this.mainLoading = false
        }
      }
    }
  },
  async activated() {
    try {
      this.loading = true
      await this.getAppList()
      if (this.appList.length > 0 && !this.search.id) {
        this.search.id = this.appList[0].id
      }
      await this.getCardData()
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
      this.openWatch = true
    }
  },
  methods: {
    async getAppList() {
      const { data } = await applicationAll()
      this.appList = data
    },
    async getCardData() {
      if (!this.search.id) {
        return []
      }
      await this.getList()
    },
    handleOperate(type, id) {
      this.$router.push({
        name: 'applicationServiceOperate',
        query: { type, appId: this.appList[this.activeName].id, id }
      })
    },
    handleDetail(id) {
      this.$router.push({ name: 'applicationServiceDetail', query: { appId: this.appList[this.activeName].id, id } })
    },
    handleSign(id) {
      this.$router.push({
        name: 'applicationServiceSign',
        query: { id }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.app-main {
  flex-wrap: wrap;
  justify-content: flex-start;
  flex-direction: row;
  display: flex;
  align-items: flex-start;
  align-content: flex-start;
  padding-left: 1%;

  .el-card {
    width: 32%;
    height: 172px;
    margin-right: 1%;
    margin-bottom: 0.8%;

    &.card-add {
      border: 1px dashed #c0ccda;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      color: #999;
      cursor: pointer;
      background-color: #fbfdff;

      i {
        margin-right: 5px;
      }

      &:hover {
        color: #409eff;
        border-color: #409eff;
      }
    }

    .app-ellipsis {
      .el-link {
        display: inline;
      }
    }
    ::v-deep .el-card__body {
      padding: 0;
    }
    &:nth-child(3n) {
      margin-right: 0;
    }
    .title {
      font-size: 16px;
      font-weight: 500;
      padding: 20px 20px 10px;
    }
    .intro {
      font-size: 14px;
      line-height: 1.6em;
      color: #999;
      margin: 10px 20px 20px;
      display: -webkit-box;
      line-clamp: 2;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      height: 3.2em;
      word-break: break-all;
    }
    .button-group {
      display: flex;
      border-top: 1px solid #e1e1e1;
      height: 45px;
      align-items: center;

      .line {
        width: 1px;
        background: #e1e1e1;
        height: 14px;
      }
      .button {
        flex: 1;
        text-align: center;

        .el-link {
          font-weight: normal;
        }
      }
    }
  }
}
</style>
