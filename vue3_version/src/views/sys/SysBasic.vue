<template>
  <div>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="部门管理" name="DepMana">
        <DepMana></DepMana>
      </el-tab-pane>
      <el-tab-pane label="职位管理" name="PosMana">
        <PosMana></PosMana>
      </el-tab-pane>
      <el-tab-pane label="职称管理" name="JobLevelMana">
        <JobLevelMana></JobLevelMana>
      </el-tab-pane>
      <el-tab-pane label="权限组" name="PositionMana">
        <PositionMana></PositionMana>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import DepMana from '@/components/sys/DepMana.vue';
import PosMana from '@/components/sys/PosMana.vue';
import JobLevelMana from '@/components/sys/JobLevelMana.vue';
import PositionMana from '@/components/sys/PositionMana.vue'

let activeName = ref('DepMana')
</script>

<style scoped>

</style>