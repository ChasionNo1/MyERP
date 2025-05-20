<script setup>
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { getAction, postAction } from '@/api/manage';
import { useRouter } from 'vue-router'
import { computed, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

defineOptions({
  name: 'LoginPage'
})

const router = useRouter()
const authStore = useAuthStore()
const loginForm = ref()
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
// 用户名校验
const validateUsername = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入用户名"));
    return;
  }

  const reg = /^[a-zA-Z0-9\u4E00-\u9FA5]{4,16}$/;
  if (!reg.test(value)) {
    callback(new Error("用户名需要4到16位不含特殊字符"));
    return;
  }

  // 校验通过
  callback();
};

// 密码校验
const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入密码"));
    return;
  }

  // 长度校验
  if (value.length < 8 || value.length > 16) {
    callback(new Error("密码长度需在8到16个字符之间"));
    return;
  }

  // 字符类型校验
  const hasLowercase = /[a-z]/.test(value); // 包含小写字母
  const hasNumber = /[0-9]/.test(value); // 包含数字
  const hasSpecial = /[^A-Za-z0-9]/.test(value); // 包含特殊符号

  if (!hasLowercase) {
    callback(new Error("密码必须包含小写字母"));
    return;
  }

  if (!hasNumber) {
    callback(new Error("密码必须包含数字"));
    return;
  }

  if (!hasSpecial) {
    callback(new Error("密码必须包含特殊符号"));
    return;
  }

  // 校验通过
  callback();
};

// 验证码校验
const validateVerifyCode = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入验证码"));
    return;
  }

  // 长度校验
  if (value.length !== 4) {
    callback(new Error("验证码长度应为4位"));
    return;
  }

  // 内容校验：4位长度，仅包含字母（大小写）或数字
  const isValid = /^[A-Za-z\d]{4}$/.test(value);
  if (!isValid) {
    callback(new Error("验证码必须为4位字母或数字"));
    return;
  }

  // 校验通过
  callback();
};

// 参数校验
const rules = {
  username: [
    {
      required: true,
      validator: validateUsername,
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      validator: validatePassword,
      trigger: "blur",
    },
  ],
  verifyCode: [
    {
      required: true,
      validator: validateVerifyCode,
      trigger: "blur",
    },
  ],
};


// 页面加载验证码
const handleChangeCheckCode = () => {
  // 调用接口获取验证码信息
  getAction('/user/verify/image', {uuid: uuid.value}).then(res => {
    if (res.code === 200) {
      // 请求成功开始赋值
      uuid.value = res.data.uuid
      verifyState.value.randCodeImage = res.data.base64Image
    } else {
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
const handleSubmit = async () => {
  // 这里需要再进行一次校验吗?
  // 要的
  try {
    const values = await loginForm.value.validateFields(
      ['username', 'password', 'verifyCode'], { force: true }
    )
    // 校验成功
    const res = await postAction('/user/login', values)
    console.log(res)
    if (res.code === 200) {
      // console.log(res.message)
      // 登录成功
      message.success(res.message || '恭喜你，登录成功')
      // 从响应头获取 Token（如 Authorization: Bearer xxx）
      // const authHeader = res.headers.get('Authorization');
      // const token = authHeader && authHeader.split(' ')[1]; // 提取 Bearer 后的 Token
      const token = res.data
  
  if (token) {
    // localStorage.setItem('accessToken', token); // 存储 Token
    // 存放到pinia中
    authStore.setToken(token)
  }
      // 跳转到首页
      router.push('/')
    } else {
      message.error(res.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:' + error)
  }


}
</script>

<template>
  <a-layout style="height: 100%; background-color: white;">
    <a-layout-header class="form-header">
      <h1>登录表单</h1>
    </a-layout-header>
    <a-layout-content>
      <a-form :model="formState" ref="loginForm" :rules="rules" name="normal_login" class="login-form"
        @finish="onFinish" @finishFailed="onFinishFailed">
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
              <a-image class="verify-code-img" alt="验证码" :preview="false" :src="verifyState.randCodeImage"
                @click="handleChangeCheckCode" fallback="@/assets/checkcode.png" />
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
          <a-button :disabled="disabled" type="primary" @click="handleSubmit" size="large" html-type="submit"
            class="login-form-button">
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
  max-width: 380px;
  /* 增加宽度以适应验证码区域 */
  margin: 0 auto;
  /* 水平居中 */
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
  padding: 0 !important;
  /* 移除form-item的内边距 */
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