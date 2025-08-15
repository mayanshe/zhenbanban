<template>
  <a-layout class="layout" :class="{ mobile: appStore.hideMenu }">
    <div v-if="navbar" class="layout-navbar">
      <NavBar />
    </div>
    <a-layout>
      <a-layout>
        <a-layout-sider
          v-if="renderMenu"
          v-show="!hideMenu"
          class="layout-sider"
          :breakpoint="'xl'"
          :collapsible="true"
          :width="menuWidth"
          :style="{ paddingTop: navbar ? '60px' : '' }"
          :hide-trigger="true"
          @collapse="setCollapsed"
        >
          <div class="menu-wrapper">
            <div class="left-side">
              <a-space style="padding-left: 5px">
                <img alt="logo" src="/src/assets/logo.png" style="width: 24px; margin-bottom: -8px" />
                <a-typography-title style="font-weight: 500; padding-left: 2px">诊伴伴</a-typography-title>
              </a-space>
            </div>
            <Menu />
          </div>
        </a-layout-sider>
        <a-drawer
          v-if="hideMenu"
          :visible="drawerVisible"
          placement="left"
          :footer="false"
          mask-closable
          :closable="false"
          @cancel="drawerCancel"
        >
          <Menu />
        </a-drawer>
        <a-layout class="layout-content" :style="paddingStyle">
          <TabBar v-if="appStore.tabBar" />
          <a-layout-content>
            <PageLayout />
          </a-layout-content>
          <div style="padding: 20px 20px; display: flex; justify-content: space-between; width: 100%">
            <div style="text-align: left">
              ©2025
              <a target="_blank" href="https://mayanshe.com">码研社</a>
              版权所有 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 本项目仅供学习交流使用, 请勿用于商业用途！
            </div>
            <div style="text-align: right; color: #ccc">
              <a href="mailto:mail@sniu.com" style="color: #888">联系我们</a>
              &nbsp;&nbsp;|&nbsp;&nbsp;
              <a target="_blank" href="https://github.com/mayanshe" style="color: #888">Repository</a>
              &nbsp;&nbsp;|&nbsp;&nbsp;
              <a target="_blank" href="https://github.com/mayanshe" style="color: #888">Issues</a>
            </div>
          </div>
        </a-layout>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import Footer from '@/components/footer/index.vue'
import Menu from '@/components/menu/index.vue'
import NavBar from '@/components/navbar/index.vue'
import TabBar from '@/components/tab-bar/index.vue'
import usePermission from '@/hooks/permission'
import useResponsive from '@/hooks/responsive'
import { useAppStore, useUserStore } from '@/store'
import { computed, onMounted, provide, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageLayout from './page-layout.vue'

const isInit = ref(false)
const appStore = useAppStore()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const permission = usePermission()
useResponsive(true)
const navbarHeight = `60px`
const navbar = computed(() => appStore.navbar)
const renderMenu = computed(() => appStore.menu && !appStore.topMenu)
const hideMenu = computed(() => appStore.hideMenu)
const footer = computed(() => appStore.footer)
const menuWidth = computed(() => {
  return appStore.menuCollapse ? 48 : appStore.menuWidth
})
const paddingStyle = computed(() => {
  const paddingLeft = renderMenu.value && !hideMenu.value ? { paddingLeft: `${menuWidth.value}px` } : {}
  const paddingTop = navbar.value ? { paddingTop: navbarHeight } : {}
  return { ...paddingLeft, ...paddingTop }
})
const setCollapsed = (val: boolean) => {
  if (!isInit.value) return // for page initialization menu state problem
  appStore.updateSettings({ menuCollapse: val })
}
watch(
  () => userStore.role,
  (roleValue) => {
    if (roleValue && !permission.accessRouter(route)) router.push({ name: 'notFound' })
  }
)
const drawerVisible = ref(false)
const drawerCancel = () => {
  drawerVisible.value = false
}
provide('toggleDrawerMenu', () => {
  drawerVisible.value = !drawerVisible.value
})
onMounted(() => {
  isInit.value = true
})
</script>

<style scoped lang="less">
@nav-size-height: 60px;
@layout-max-width: 1100px;

.layout {
  width: 100%;
  height: 100%;

  .layout-sider {
    background: var(--color-menu-dark-bg);
    position: fixed;
    top: 0;
    left: 0;
    z-index: 99;
    height: 100%;
    padding-top: 0 !important;

    .left-side {
      display: flex;
      align-items: center;
      padding-left: 8px;
      background: var(--color-menu-dark-bg);
      height: 60px;

      .arco-typography {
        color: #fff;
        font-size: 18px;
        width: 200px;
      }
    }

    &::after {
      position: absolute;
      top: 0;
      right: -1px;
      display: block;
      width: 1px;
      height: 100%;
      background-color: var(--color-border);
      content: '';
    }

    > :deep(.arco-layout-sider-children) {
      overflow-y: hidden;
      top: 0;
    }

    .menu-wrapper {
      height: 100%;
      overflow: auto;
      overflow-x: hidden;

      :deep(.arco-menu) {
        height: calc(100% - 60px) !important;

        ::-webkit-scrollbar {
          width: 12px;
          height: 4px;
        }

        ::-webkit-scrollbar-thumb {
          border: 4px solid transparent;
          background-clip: padding-box;
          border-radius: 7px;
          background-color: var(--color-text-4);
        }

        ::-webkit-scrollbar-thumb:hover {
          background-color: var(--color-text-3);
        }
      }
    }
  }

  .layout-content {
    min-width: @layout-max-width;
    min-height: 100vh;
    overflow-y: hidden;
    background-color: var(--color-fill-2);
    transition: all 0.2s cubic-bezier(0.34, 0.69, 0.1, 1);

    .layout-navbar {
      transition: all 0.2s cubic-bezier(0.34, 0.69, 0.1, 1);
      position: fixed;
      top: 0;
      left: 250px;
      z-index: 100;
      width: 100%;
      min-width: @layout-max-width;
      height: @nav-size-height;
    }
  }

  .arco-layout-sider-collapsed {
    .left-side {
      width: 50px;

      .arco-typography {
        color: transparent;
      }
    }

    + .layout-content {
      .layout-navbar {
        left: 50px !important;

        .navbar {
          width: calc(100% - 50px) !important;
        }
      }
    }
  }
}
</style>
