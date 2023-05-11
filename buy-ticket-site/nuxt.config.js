module.exports = {
  /*
  ** Headers of the page
  */
  configureWebpack: {
    //关闭 webpack 的性能提示
    performance: {

      hints: "warning", // 枚举
    
      maxAssetSize: 300000, // 整数类型（以字节为单位）
    
      maxEntrypointSize: 500000, // 整数类型（以字节为单位）
    
      assetFilter: function(assetFilename) {
        // 提供资源文件名的断言函数
        return assetFilename.endsWith('.css') || assetFilename.endsWith('.js');
      }
    }
  },

  head: {
    title: 'buy-ticket-site',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '电影院线上购票网站' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },

  /*
  ** Customize the progress bar color
  */
  loading: { color: '#3B8070' },
  /*
  ** Build configuration
  */
  build: {
    /*
    ** Run ESLint on save
    */
    extend (config, { isDev, isClient }) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    },
    postcss: null
  },
  plugins:[
    { src: '~/plugins/myPlugin.js', ssr:false}
  ]
}

