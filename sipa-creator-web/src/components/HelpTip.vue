<template>
  <el-tooltip :content="content">
    <i class="el-icon-question" style="cursor:hand;"></i>
  </el-tooltip>
</template>

<script>
export default {
  props: ["content"]
};
</script>
