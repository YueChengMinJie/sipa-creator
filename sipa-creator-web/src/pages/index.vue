<template>
  <div class="wrapper">
    <nav class="main-header navbar navbar-expand-md navbar-light navbar-white">
      <div class="container">
        <a href="javascript:;" class="navbar-brand">
          <img
              alt="Mybatis-Plus代码生成器"
              src="~/assets/img/logo.png"
              class="brand-image img-circle elevation-3"
              style="opacity: .8"
          />
          <span class="brand-text font-weight-light">Sipa Creator</span>
        </a>
        <div class="collapse navbar-collapse order-3" id="navbarCollapse">
          <!-- Left navbar links -->
          <ul class="navbar-nav">
            <li class="nav-item dropdown">
              <a
                  id="tableMenuDropdown"
                  href="#"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                  class="nav-link dropdown-toggle"
              >Table代码生成</a>
              <ul aria-labelledby="tableMenuDropdown" class="dropdown-menu border-0 shadow">
                <li>
                  <router-link to="/table" class="dropdown-item">Table查询</router-link>
                </li>
                <li>
                  <router-link to="/config" class="dropdown-item">输出配置</router-link>
                </li>
              </ul>
            </li>
            <li class="nav-item">
              <router-link to="/sql" class="nav-link">SQL代码生成</router-link>
            </li>
            <li class="nav-item">
              <el-select v-model="tenant" @change="selectTenant">
                <el-option
                    v-for="tenant in tenants"
                    :key="tenant.value"
                    :label="tenant.label"
                    :value="tenant.value">
                </el-option>
              </el-select>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" v-cloak>
      <nuxt-child :key="viewKey"/>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import store from "@/store";

export default {
  props: [],
  beforeDestroy: function () {
  },
  computed: {
    viewKey() {
      return this.$route.name !== undefined
          ? this.$route.name + +new Date()
          : this.$route + +new Date();
    }
  },
  data() {
    return {
      tenant: null,
      tenants: null
    };
  },
  mounted: function () {
    this.getTenants();
  },
  methods: {
    getTenants() {
      axios.get("/api/tenants").then((tenants) => {
        const tenant = tenants[0].value;
        this.tenant = tenant
        this.tenants = tenants;
        store().commit('selectTenant', tenant)
      });
    },
    selectTenant(tenant) {
      this.tenant = tenant
      store().commit('selectTenant', tenant)
    }
  }
};
</script>

<style>
</style>
