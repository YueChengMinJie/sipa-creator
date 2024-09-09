<template>
  <div class="user-info">
    <el-popover
      placement="bottom"
      width="320"
      transition="el-zoom-in-left"
      title="个人信息"
      trigger="hover"
      class="popover"
    >
      <!-- 个人信息 end -->
      <div class="avatar" slot="reference">
        <el-image
          v-if="!userInfo.headImg || userInfo.headImg === '1'"
          :src="require('@/assets/default-avatar1.jpg')"
          fit="cover"
        ></el-image>
        <el-image
          v-else-if="userInfo.headImg === '2'"
          :src="require('@/assets/default-avatar2.jpg')"
          fit="cover"
        ></el-image>
        <el-image v-else :src="userInfo.headImg" fit="cover"></el-image>
        <i class="el-icon-arrow-down"></i>
      </div>
      <!-- 个人信息 -->
      <div class="layout-personal">
        <div class="avatar">
          <div class="about">
            <el-image
              v-if="!userInfo.headImg || userInfo.headImg === '1'"
              :src="require('@/assets/default-avatar1.jpg')"
              fit="cover"
            ></el-image>
            <el-image
              v-else-if="userInfo.headImg === '2'"
              :src="require('@/assets/default-avatar2.jpg')"
              fit="cover"
            ></el-image>
            <el-image v-else :src="userInfo.headImg" fit="cover"></el-image>
            <div class="about-right">
              <!-- row -->
              <div class="intro">
                <div class="part part-row">
                  <span class="tit">手机号:</span>
                  <span class="txt omit">{{ userInfo.phoneNo || '--' }}</span>
                </div>
              </div>
              <!-- row end -->
              <!-- row -->
              <div class="intro">
                <div class="part part-row">
                  <span class="tit">真实姓名:</span>
                  <span class="txt omit">{{ userInfo.name || '--' }}</span>
                </div>
              </div>
              <!-- row end -->
              <!-- row -->
              <div class="intro">
                <div class="part part-row">
                  <span class="tit">当前角色:</span>
                  <span class="txt omit">
                    {{ filterRoles(userInfo.roles) }}
                  </span>
                </div>
              </div>
              <!-- row end -->
            </div>
          </div>
          <!-- row -->
          <div class="intro">
            <div class="part part-row">
              <span class="tit">本次登录时间:</span>
              <span class="txt omit">{{ dateTimeFilter(userInfo.loginTime) }}</span>
            </div>
          </div>
          <!-- row end -->
          <!-- row -->
          <div class="intro">
            <div class="part part-row">
              <span class="tit">本次登录IP地址:</span>
              <span class="txt omit">{{ userInfo.loginIp || '--' }}</span>
            </div>
          </div>
          <!-- row end -->
          <!-- row -->
          <div class="intro">
            <div class="part part-row">
              <span class="tit">上次登录时间:</span>
              <span class="txt omit">{{ dateTimeFilter(userInfo.lastLoginTime) }}</span>
            </div>
          </div>
          <!-- row end -->
          <!-- row -->
          <div class="intro">
            <div class="part part-row">
              <span class="tit">上次登录IP地址:</span>
              <span class="txt omit">{{ userInfo.lastLoginIp || '--' }}</span>
            </div>
          </div>
          <!-- row end -->
          <!-- 菜单 -->
          <div class="menu-btn">
            <el-button plain type="primary" @click="handleUserInfo()">个人中心</el-button>
            <el-button v-if="userInfo.ifMultiTenant" plain type="warning" @click="handleSwitchApp()" :loading="loading">
              切换应用
            </el-button>
            <el-button plain type="danger" @click="handleSignOut()" :loading="loading">
              {{ loading ? '正在退出' : '退出登录' }}
            </el-button>
          </div>
          <!-- 菜单 end -->
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script>
import { dateTimeStr } from '@/utils/commonTools'
import { logout } from '@/api/sso-idp-server'
import Config from '@/config'
import Cookies from 'js-cookie'

export default {
  data() {
    return {
      loading: false,
      isSpecial: false
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    }
  },
  created() {},
  methods: {
    dateTimeFilter(date) {
      return date ? dateTimeStr(date) : '--'
    },
    // 登出
    async handleSignOut() {
      try {
        this.loading = true
        await logout()
        this.$store.commit('removeToken')
        // 退出登陆，用系统跳转，释放项目缓存
        location.replace(Config.loginUrl)
      } catch (error) {
        console.error('await:', error)
        this.loading = false
      }
    },
    // 切换应用
    handleSwitchApp() {
      try {
        const params = new URLSearchParams(window.location.search)
        const key = params.get(Config.identityId)
        const jsonData = Cookies.get(key)
        const loginData = JSON.parse(jsonData)
        location.href = `${Config.loginUrl}/platform?token=${loginData.token}&ifFree=${loginData.ifFree}`
      } catch (error) {
        console.error('await:', error)
        this.$notify({
          title: '消息',
          message: '获取登录信息异常, 无法切换应用!',
          type: 'error'
        })
      }
    },
    filterRoles(roles) {
      if (roles && roles.length > 0) {
        const roleNames = roles.map(item => item.name)
        return roleNames.join(' / ')
      } else {
        return '--'
      }
    },
    handleUserInfo() {
      this.$router.push({ name: 'systemUserInfo' })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info {
  display: flex;
  align-items: center;

  .popover {
    display: block;
    height: 100%;

    .avatar {
      &:hover {
        background-color: #ecf5ff;
      }
      &:hover .el-icon-arrow-down {
        transform: rotate(180deg);
      }
    }
  }
}
.avatar {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  height: 100%;
  transition: ease-out 0.3s;

  .el-icon-arrow-down {
    font-size: 14px;
    margin-left: 5px;
    transition: ease-out 0.3s;
  }
  .el-image {
    width: 40px;
    height: 40px;
    border-radius: 4px;
    text-align: center;
    border: 1px solid #e1e1e1;
    padding: 2px;
  }
  .name {
    margin: 0 5px 0 8px;
  }
}
.layout-personal {
  display: flex;
  padding: 10px;
  line-height: 1.7em;
  justify-content: space-between;

  .avatar {
    font-size: 12px;
    display: flex;
    flex: 1;
    flex-direction: column;
    align-items: center;
    color: #606266;
    cursor: initial;

    .about {
      display: flex;
      width: 100%;
      margin-bottom: 15px;

      .about-right {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }
    }
    .el-image {
      width: 80px;
      height: 80px;
      margin-right: 15px;
      border-radius: 4px;
      text-align: center;
    }
    .name {
      font-weight: bold;
      text-align: center;
      margin: 10px 0;
      font-size: 14px;
    }
    .intro {
      width: 100%;
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;

      .part {
        width: 48%;
        overflow: hidden;
        display: flex;

        .tit {
          font-weight: bold;
          margin-right: 4px;
        }
        .txt {
          flex: 1;
          text-align: right;
        }
      }
      .part-row {
        width: 100%;
      }
    }
    .menu-btn {
      width: 100%;
      display: flex;
      justify-content: space-between;
      margin-top: 15px;

      .el-button {
        flex: 1;
      }
    }
  }
}
</style>
