<template>
  <div class="container">
    <Breadcrumb :items="['组织', '医院']" page-name="医院" route-name="HospitalManage" />
    <a-card class="general-card" title="医院管理"></a-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'HospitalManage',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
