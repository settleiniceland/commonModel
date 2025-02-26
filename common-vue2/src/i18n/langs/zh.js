import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
import Common from './common/index'
import Menu from './menu/index'
const zh = {
    message: {
        Mark: 'zh',
        Button: Common.Button,
        Menu: Menu.Menu,
    },
    ...zhLocale
}
export default zh