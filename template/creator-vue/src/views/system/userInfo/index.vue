<!--
 * @Author: caszhou
 * @Date: 2021-05-11 14:44:15
 * @Description: 用户中心
-->

<template>
  <div class="app-container">
    <div class="intro">
      <div class="avatar">
        <el-image
          v-if="!userInfo.headImg || userInfo.headImg === '1'"
          :src="require('@/assets/default-avatar1.jpg')"
          fit="cover"
          class="img"
        ></el-image>
        <el-image
          v-else-if="userInfo.headImg === '2'"
          :src="require('@/assets/default-avatar2.jpg')"
          fit="cover"
          class="img"
        ></el-image>
        <el-image v-else :src="userInfo.headImg" fit="cover" class="img"></el-image>
        <p @click="handleDialogShow('avatarVisible')">
          <span>修改头像</span>
        </p>
      </div>
      <div class="info">
        <p>手机号: {{ userInfo.phoneNo || '--' }}</p>
        <p>上次登录时间: {{ userInfo.lastLoginTime ? dateTimeStr(userInfo.lastLoginTime) : '--' }}</p>
        <p>上次登录IP: {{ userInfo.lastLoginIp || '--' }}</p>
      </div>
    </div>
    <!-- section -->
    <div class="section">
      <div class="text">
        <span>手机号</span>
        <p>您可以修改手机号，便于记忆。系统中账号唯一。</p>
      </div>
      <div class="setting">
        <template>
          <el-button type="primary" size="small" style="margin-left: 30px" @click="handleUserEdit()" disabled>
            修改
          </el-button>
        </template>
      </div>
    </div>
    <div class="section">
      <div class="text">
        <span>登录密码</span>
        <p>
          安全性高的密码可以使账号更安全。建议您定期更换密码, 设置一个包含字母, 符号或数字中至少两项且长度超过6位的密码
        </p>
      </div>
      <div class="setting">
        <template>
          <el-button type="primary" size="small" style="margin-left: 30px" @click="handleDialogShow('passwordVisible')">
            修改
          </el-button>
        </template>
      </div>
    </div>
    <unit-avatar v-model="avatarVisible" />
    <unit-user v-model="userEditVisible" />
    <unit-password v-model="passwordVisible" />
  </div>
</template>

<script>
import { dateTimeStr } from '@/utils/commonTools'
import UnitAvatar from './UnitAvatar'
import UnitPassword from './UnitPassword'
import UnitUser from './UnitUser'

export default {
  components: {
    UnitAvatar,
    UnitPassword,
    UnitUser
  },
  data() {
    return {
      avatarShow: false,
      passwordVisible: false,
      userEditVisible: false,
      avatarVisible: false
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    }
  },
  mounted() {},
  methods: {
    dateTimeStr,
    handleDialogShow(key) {
      this[key] = true
    },
    handleUserEdit() {
      if (!this.userInfo.phoneNo) {
        this.$message.warning('您需要先绑定手机号')
        return false
      }
      this.userEditVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.intro {
  display: flex;
  padding: 0 26px 20px 26px;
  border-bottom: 1px dashed #bbb;

  .avatar {
    text-align: center;
    width: auto;
    height: auto;

    .img {
      width: 116px;
      height: 116px;
      margin: 0;
      padding: 0;
      display: block;
      border-radius: 50%;
      object-fit: cover;
    }
    p {
      color: #428df2;
      font-size: 14px;
      margin-top: 10px;

      span {
        cursor: pointer;
      }
    }
  }
  .info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    font-size: 12px;
    padding-left: 40px;

    p {
      padding: 0;
      margin: 6px 0;
    }
  }
}
.section {
  padding: 30px 40px 30px 20px;
  display: flex;
  align-items: center;
  border-bottom: 1px dashed #bbb;
  color: #484848;

  .text {
    flex: 1;
    display: flex;
    line-height: 25px;

    span {
      font-size: 16px;
      width: 150px;
    }
    p {
      font-size: 14px;
      padding: 0;
      margin: 0;
      flex: 1;
    }
  }
  .setting {
    width: 180px;
    line-height: 32px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
