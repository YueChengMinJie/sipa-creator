<!--
 * @Author: caszhou
 * @Date: 2021-03-26 15:00:28
 * @Description: 根节点
-->

<template functional>
  <div id="app">
    <transition
      mode="out-in"
      enter-active-class="animated fadeIn"
      leave-active-class="animated fadeOut"
      :duration="{ enter: 200, leave: 200 }"
    >
      <router-view />
    </transition>
  </div>
</template>
