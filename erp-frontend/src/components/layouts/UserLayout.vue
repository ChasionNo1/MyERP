<script setup>
import { ref, onMounted, onBeforeUnmount, onBeforeMount } from "vue";
defineOptions({
  name: "UserLayout",
});
// 定义可见性
const showMap = ref({
  isShowRight: false,
  isAndroidShow: false,
  isIPhoneShow: false,
  isMiniProgramShow: false,
});
const systemTitle = ref(window.SYS_TITLE);
const systemUrl = ref(window.SYS_URL);
const systemVersion = ref(window.SYS_VERSION);

// 生命周期钩子
onMounted(() => {
  document.body.classList.add("userLayout");
});

onBeforeUnmount(() => {
  document.body.classList.remove("userLayout");
});

onBeforeMount(() => {
  const host = window.location.host;
  showMap.value.isShowRight = host === "localhost:5173";
});

</script>

<template>
  <div class="back-layout">
    <div id="userLayout" :class="['user-layout-wrapper', device]">
      <a-layout style="height: 100%;">
      <a-layout-content class="container" >
        <a-row style="height: 100%;">
          <a-col :span="14" class="bg-pic"></a-col>
          <a-col :span="6" :offset="2" class="main-form">
            <!-- 登录或者注册的表单 -->
            <div class="form">
              <router-view></router-view>
            </div>
          </a-col>
        </a-row>
      </a-layout-content>
      <a-layout-footer class="footer" >Footer</a-layout-footer>
      </a-layout>
    </div>
  </div>
</template>

<style scoped>
.back-layout {
  height: 100%;
  width: 100%;
  overflow: hidden;
  /* background-color: palegoldenrod; */
}
.user-layout-wrapper {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.container {
  flex: 95%; /* 占据90%的剩余空间 */
  width: 100%;
  /* background-color: aquamarine; */
}
.bg-pic {
  height: 100%;
  background: url('/static/login-bg.jpeg') no-repeat;
  background-size: cover; /* 确保背景图覆盖容器 */
}

.main-form {
  height: 100%;
  display: flex; /* 使用flex布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  padding: 20px; /* 增加内边距防止边缘溢出 */
}

.form {
  width: 100%; /* 宽度为父容器的80% */
  max-width: 400px; /* 最大宽度限制 */
  height: 85%; /* 高度为父容器的60% */
  max-height: 600px; /* 最大高度限制 */
  background-color: white; /* 表单背景色 */
  border-radius: 10px; /* 圆角 */
  padding: 20px; /* 内边距 */
  box-shadow: 0 4px 12px rgba(0,0,0,0.1); /* 阴影效果 */
}

.footer {
  flex:5%; /* 占据10的剩余空间 */
  text-align: center;
  background-color: rgb(138, 138, 232);
}

</style>
<style lang="less" scoped>
 
</style>
