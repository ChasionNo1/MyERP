<script setup>
import { UserOutlined, LockOutlined} from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { getAction } from '@/api/manage';
defineOptions({
  name: 'LoginPage'
})
import { computed, ref } from 'vue';

const form = ref()
const formState = ref({
  username: '',
  password: '',
  verifyCode: '',
  remember: false,
});
// 跟随验证码的uuid
const uuid = ref('')
const verifyState = ref({
  randCodeImage: '',
})
const onFinish = values => {
  console.log('Success:', values);
};
const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);
};
const disabled = computed(() => {
  return !(formState.value.username && formState.value.password);
});

// 参数校验
const rules = {
  username: [
    {
      required:true,
      message: '请输入有效用户名',
      trigger: 'change',
    },
    {
      min: 3,
      max: 16,
      message: '输入长度应该在3到16之间',
      trigger: 'blur',
    },
  ],
  password: [
     {
      required: true,
      message: '请输入有效密码',
      trigger: 'change',
    },
    {
      min: 3,
      max: 16,
      message: '输入长度应该在3到16之间',
      trigger: 'blur',
    },
  ],
  verifyCode: [
    {
      required: true,
      message: '请输入有效验证码',
      trigger: 'blur',
    },
    {
      min: 4,
      max: 4,
      message: '请输入4位验证码',
      trigger: 'blur'
    }
  ]
}

// 页面加载验证码
const handleChangeCheckCode = () => {
  // 调用接口获取验证码信息
  getAction('/user/randomImage').then(res=>{
    if (res.code === 200) {
      // 请求成功开始赋值
      uuid.value = res.data.uuid
      verifyState.value.randCodeImage = res.data.base64
    }else {
      message.error(res.data)
    }
  }).catch((error) => {
    // 出错的回调函数，这里不需要啥操作了，因为使用了a-image标签
    console.error('获取验证码失败:', error);
  })
}
// 进入页面调用一次请求验证码
handleChangeCheckCode()


// 处理登录的函数
// 表单里有哪些内容：用户名、密码、验证码、记住我, uuid
// 都传给后端,这些数据在formState里都有
const handleSubmit = () => {
  // 这里需要再进行一次校验吗?
  // 要的
  form.value.validateFields(['username', 'password', 'verifyCode'], {force: true}, 
  (err, values) => {
      if (!err) {
        // 校验成功
        let loginParams = {
          username: values.username,
          password: values.password,
          verifyCode: values.verifyCode,
          uuid: uuid.value
        }
        // 发送登录请求,接口来自于store中action


      }
  })



}
</script>

<template>
  <a-layout style="height: 100%; background-color: white;">
    <a-layout-header class="form-header"><h1>登录表单</h1></a-layout-header>
    <a-layout-content>
      <a-form :model="formState" ref="form" :rules="rules" name="normal_login" class="login-form" @finish="onFinish" 
        @finishFailed="onFinishFailed">
        <a-form-item name="username">
          <a-input v-model:value="formState.username" size="large">
            <template #prefix>
              <UserOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password">
          <a-input-password v-model:value="formState.password" size="large">
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>
        <!-- 验证码行 - 使用 a-row 和 a-col 布局 -->
        <a-form-item name="verifyCode">
          <a-row gutter="16">
            <a-col :span="12">
              <a-input v-model:value="formState.verifyCode" size="large" placeholder="请输入验证码"></a-input>
            </a-col>
            <a-col :span="12">
              <a-image class="verify-code-img" alt="验证码" :src="verifyState.randCodeImage" @click="handleChangeCheckCode" fallback="@/assets/checkcode.png" />
              <!-- <img src="https://picsum.photos/120/40" alt="验证码" class="verify-code-img" /> -->
            </a-col>
          </a-row>
        </a-form-item>

        <!-- 记住我和忘记密码行 - 使用flex布局 -->
        <a-form-item class="remember-row-wrapper">
          <div class="remember-row">
            <a-form-item name="remember" no-style>
              <a-checkbox v-model:checked="formState.remember">记住我</a-checkbox>
            </a-form-item>
            <a class="login-form-forgot" href="">忘记密码</a>
          </div>
        </a-form-item>

        <a-form-item class="login-btn-row">
          <a-button :disabled="disabled" type="primary" @click="handleSubmit" size="large" html-type="submit" class="login-form-button">
            登录
          </a-button>
        </a-form-item>
      </a-form>
    </a-layout-content>
    <a-layout-footer class="form-footer">
      还没有账号?<router-link to="/user/register">立即注册</router-link>
    </a-layout-footer>
  </a-layout>
</template>

<style scoped>
.form-header {
  background-color: white;
  text-align: center;
}
.login-form {
  max-width: 380px; /* 增加宽度以适应验证码区域 */
  margin: 0 auto; /* 水平居中 */
}

/* 验证码图片样式 */
.verify-code-img {
  width: 100%;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
}

/* 修复记住我/忘记密码行的两端对齐 */
.remember-row-wrapper {
  padding: 0 !important; /* 移除form-item的内边距 */
}

.remember-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

/* 登录按钮样式 */
.login-btn-row {
  margin-bottom: 0;
  width: 100%;
}
/* 登录按钮占满宽度 */
.login-form-button {
  width: 100%;
}

/* 页脚样式 */
.form-footer {
  text-align: center;
  background-color: white;
  padding: 16px 0;
}

</style>