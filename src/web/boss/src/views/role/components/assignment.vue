<template>
  <a-modal :width="800" v-model:visible="dialog.open" @close="handleClose" unmountOnClose>
    <template #title>
      <div class="title">
        配置资源及权限 :&nbsp;&nbsp;&nbsp;&nbsp;
        <span style="color: #20b99b">{{ role.displayName }}</span>
      </div>
    </template>
    <div class="box">
      <div class="section">配置资源：</div>
      <a-checkbox-group style="width: 100%" :default-value="assignment.resourceIds">
        <div class="resource">
          <div class="item first" v-for="item in resources" :key="item.id">
            <div class="row">
              <div class="name" @click="item.collapse = !item.collapse">
                <icon-plus v-show="item.children.length > 0 || item.buttons.length > 0" v-if="!item.collapse" />
                <icon-minus v-show="item.children.length > 0 || item.buttons.length > 0" v-else />
                {{ item.name }}
                <span style="color: #898989">{{ item.path }}</span>
              </div>
              <div class="check" style="margin-right: -12px"><a-checkbox :value="item.id" /></div>
            </div>
            <div v-show="item.collapse" class="resource">
              <div class="buttons" v-if="item.buttons.length > 0" style="display: flex; align-items: center">
                <div class="line"></div>
                <a-checkbox v-for="fbt in item.buttons" :key="fbt.id" :value="fbt.id">
                  <span style="color: #676767">{{ fbt.name }}</span>
                </a-checkbox>
              </div>
              <div class="item second" v-for="second in item.children" :key="item.id">
                <div class="row">
                  <div class="name" @click="second.collapse = !second.collapse">
                    <icon-minus v-show="second.children.length > 0 || second.buttons.length > 0" v-if="!second.collapse"/>
                    <icon-plus v-show="second.children.length > 0 || second.buttons.length > 0"  v-else />
                    {{ second.name }}
                    <span style="color: #898989">{{ second.path }}</span>
                  </div>
                  <div class="check" style="margin-right: -12px"><a-checkbox :value="second.id"></a-checkbox></div>
                </div>
                <div v-show="!second.collapse" class="resource">
                  <div class="buttons" v-if="second.buttons.length > 0" style="display: flex; align-items: center">
                    <div class="line"></div>
                    <a-checkbox v-for="sbt in second.buttons" :key="sbt.id" :value="sbt.id">
                      <span style="color: #676767">{{ sbt.name }}</span>
                    </a-checkbox>
                  </div>
                  <div class="item third" v-for="third in second.children" :key="item.id">
                    <div class="row">
                      <div class="name" @click="third.collapse = !third.collapse">
                        <icon-minus v-show="third.children.length > 0 || third.buttons.length > 0" v-if="!third.collapse"  />
                        <icon-plus v-show="third.children.length > 0 || third.buttons.length > 0" />
                        {{ third.name }} {{ second.path }}
                      </div>
                      <div class="check" style="margin-right: -12px">
                        <a-checkbox :value="third.id"></a-checkbox>
                      </div>
                    </div>
                    <div
                      class="buttons third-buttons"
                      v-if="!third.collapse && third.buttons.length > 0"
                      style="display: flex; align-items: center; padding-left: 18px"
                    >
                      <div class="line"></div>
                      <a-checkbox v-for="tbt in third.buttons" :key="tbt.id" :value="tbt.id">
                        <span style="color: #676767">{{ tbt.name }}</span>
                      </a-checkbox>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </a-checkbox-group>

      <div class="section" style="margin-top: 12px">配置权限：</div>
      <a-checkbox-group style="width: 100%" :default-value="assignment.permissionIds">
        <div class="resource">
          <div class="item first" v-for="item in groupedPermissions.filter(record => record.children.length > 0 || record.permissions.length > 0)" :key="item.groupId">
            <div class="row">
              <div class="name" @click="item.collapse = !item.collapse">
                <icon-plus  v-if="!item.collapse" />
                <icon-minus v-else />
                {{ item.groupDisplayName }}
              </div>
            </div>
            <div v-show="item.collapse" class="resource">
              <div class="buttons" v-if="item.permissions.length > 0" style="display: flex; align-items: center">
                <div class="line"></div>
                <a-checkbox v-for="fpm in item.permissions" :key="fpm.id" :value="fpm.id">
                  <span style="color: #676767">{{ fpm.displayName }}</span>
                </a-checkbox>
              </div>
              <div class="item second" v-for="first in item.children.filter(record => record.children.length > 0 || record.permissions.length > 0)" :key="first.groupId">
                <div class="row">
                  <div class="name" @click="first.collapse = !first.collapse">
                    <icon-minus v-if="!first.collapse" />
                    <icon-plus v-else />
                    {{ first.groupDisplayName }}
                  </div>
                </div>
                <div v-show="!first.collapse" class="resource">
                  <div class="buttons" v-if="first.permissions.length > 0" style="display: flex; align-items: center">
                    <div class="line"></div>
                    <a-checkbox v-for="fpm in first.permissions" :key="fpm.id" :value="fpm.id">
                      <span style="color: #676767">{{ fpm.displayName }}</span>
                    </a-checkbox>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </a-checkbox-group>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'
import { Message } from '@arco-design/web-vue'
import { RoleView, getRole } from '@/api/role'
import { ResourceView, ResourceList, getResourceList } from '@/api/resource'
import { GroupedPermission, getGroupedPermissionList } from '@/api/permission'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
  roleId: string // 角色ID
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  remark: '资源和权限分开配置，权限仅用于控制接口，不单独对查询设置权限，满足权限组中的任何一权限即获得权限。',
})

watch(
  () => props.open,
  (obj) => {
    dialog.value.open = props.open

    if (obj) {
      loadRole()
      loadResourceList()
      loadGroupedPermissions()
    }
  }
)

const handleClose = async () => {
  emit('update:open', false)
}

// region 加载角色
const role = ref<RoleView>({
  id: '0',
  roleName: '',
  displayName: '',
  description: '',
  createdAt: '',
  updatedAt: '',
  permissionIds: [] as string[],
  resourcesIds: [] as string[],
})

const loadRole = async () => {
  getRole(props.roleId)
    .then((res: RoleView) => {
      role.value = res
      assignment.value.resourceIds = res.resourcesIds
      assignment.value.permissionIds = res.permissionIds
    })
    .catch(() => {
      handleClose()
    })
}
// endregion

// region 加载资源
const resources = ref<ResourceList[]>([])

const transResourceList = (data: ResourceView, kind: string) => {
  if (data.length === 0) {
    return []
  }

  const list = []
  let item
  for (let i = 0; i < data.length; i += 1) {
    item = data[i]
    if (kind === 'button' && item.resourceType.code.toLowerCase() === 'button') {
      if (item.resourceType.code.toLowerCase() === 'button') {
        list.push({
          id: item.id,
          name: item.displayName,
        })
      }
    }

    if (kind !== 'button' && item.resourceType.code.toLowerCase() !== 'button') {
      list.push({
        id: item.id,
        name: item.displayName,
        path: item.resourceType.code === 'url' ? item.url : item.path,
        buttons: transResourceList(item.children, 'button'),
        children: transResourceList(item.children, 'resource'),
      })
    }
  }

  return list
}

const loadResourceList = async () => {
  getResourceList()
    .then((res) => {
      resources.value = transResourceList(res, 'resource')
    })
    .catch(() => {
      resources.value = []
    })
}
// endregion

// region 加载分组权限组
const groupedPermissions = ref<GroupedPermission[]>([])

const loadGroupedPermissions = async () => {
  getGroupedPermissionList()
    .then((res) => {
      groupedPermissions.value = res
    })
    .catch(() => {
      groupedPermissions.value = []
    })
}
// endregion

// region 表单提交
const generateAssignment = () => {
  return {
    resourceIds: [] as string[],
    permissionIds: [] as string[]
  }
}

const assignment = ref(generateAssignment());

// endregion
</script>

<script lang="ts">
export default {
  name: 'RoleAssignment',
}
</script>

<style scoped lang="less">
.remark {
  margin-bottom: 12px;
}
.title {
  width: 100%;
}

.box {
  min-height: 300px;
  max-height: 600px;
  overflow-y: auto;
}

.top {
  margin-bottom: 16px;
}

.section {
  color: #0960bd;
  font-size: 15px;
  margin-bottom: 12px;
}

.resource {
  font-size: 13px;
  margin-top: 0px;
  margin-bottom: 12px;
}

.resource .name {
  cursor: pointer;
}

.resource .row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.first > .row {
  background: rgb(242, 243, 245);
  padding: 4px 12px;
  border-radius: 2px;
  margin-bottom: 6px;
}

.second > .row {
  padding: 4px 12px;
  border-radius: 2px;
}

.third > .row {
  padding: 4px 12px;
  border-radius: 2px;
  margin-bottom: 6px;
}

.third > .buttons {
  margin-top: -6px;
}

.resource .buttons {
  margin-left: 6px;
  padding-left: 14px;
}

.first > .resource {
  padding-left: 30px;
}

.second > .resource {
  padding-left: 18px;
  margin-bottom: 0;
}

.arco-checkbox {
  font-size: 13px;
}

.buttons {
  position: relative;
}

.buttons .line {
  position: absolute;
  width: 12px;
  height: 18px;
  z-index: 100;
  border-left: dotted 1px #898989;
  border-bottom: dotted 1px #898989;
  left: 1px;
  top: -6px;
}

.third-buttons {
  padding-left: 32px !important;
}

.third-buttons .line {
  left: 18px;
}

.buttons label {
  min-width: 120px;
}
</style>
