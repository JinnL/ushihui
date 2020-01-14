package com.ekabao.oil.bean;

import java.util.List;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2019/1/8 17:35
 * Created by lj on 2019/1/8 17:35.
 */

public class GoodsDetails {


    /**
     * specificationList : [{"specification_id":4,"valueList":[{"goodsId":1181000,"id":1,"picUrl":"","specificationId":4,"value":"1200g"},{"goodsId":1181000,"id":3,"picUrl":"http://yanxuan.nosdn.127.net/10022c73fa7aa75c2c0d736e96cc56d5.png?quality=90&amp;thumbnail=200x200&amp;imageView","specificationId":4,"value":"500g"}],"name":"重量"},{"specification_id":5,"valueList":[{"goodsId":1181000,"id":5,"picUrl":"http://yanxuan.nosdn.127.net/36f64a7161b67e7fb8ea45be32ecfa25.png?quality=90&amp;thumbnail=200x200&amp;imageView","specificationId":5,"value":"XXL"},{"goodsId":1181000,"id":6,"specificationId":5,"value":"XL"}],"name":"大小"},{"specification_id":6,"valueList":[{"goodsId":1181000,"id":7,"specificationId":6,"value":"白色"},{"goodsId":1181000,"id":8,"specificationId":6,"value":"黑色"}],"name":"颜色"}]
     * info : {"addTime":1504064411000,"appExclusivePrice":0,"attributeCategory":1036000,"brandId":1001020,"categoryId":1011000,"counterPrice":0,"extraPrice":0,"goodsBrief":"安心舒适是最好的礼物","goodsDesc":"<p><img src=\"http://yanxuan.nosdn.127.net/3ddfe10db43f7df33ba82ae7570ada80.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/7682b7930b4776ce032f509c24a74a1e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e0bb6a50e27681925c5bb26bceb67ef4.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/ba63b244c74ce06fda82bb6a29cc0f71.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/3c7808c3a4769bad5af4974782f08654.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/71798aaac23a91fdab4d77e1b980a4df.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c88cbb2dd2310b732571f49050fe4059.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/5dfdcd654e0f3076f7c05dd9c19c15ea.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/bd55d6ef7af69422d8d76af10ee70156.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/bae571b22954c521b35e446d652edc1d.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e709c4d9e46d602a4d2125e47110f6ae.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/83e41915035c418db177af8b1eca385c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/f42c561e6935fe3e0e0873653da78670.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/8317726fbae80b98764dc4c6233a913e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/ba904b7c948b8015db2171435325270f.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/4969c73d0d8f29bffb69529c96ca4889.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/d80b9b8c5c31031d1cd5357e48748632.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/3fe79bdae40662a7b1feed3179d3dd1f.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/79eef059963b12479f65e782d1dca128.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e5a8f64f4297ccc01b41df98b0f252c8.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/a940b9e9525c4861407e4c3b07b02977.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/224b8b81cbe12e4ad060a50f1e26601a.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/85e151647452fad718effb7b1adc18e2.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/d47444ff3bb9dc0aa4ab1f9b84d83768.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/136262743f0c849cc0c55c8e7963dd7e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/331a97cbaff5b25a3b08ed7cdfe29df9.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/89b450aa0a8afe1db566dcad926f1fe8.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c1cf94f13b7280a97e751cebe573fa78.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/1822c23def83b77e7607c24237eeec74.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/2af234312b3914d6d0bc316f92134614.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c4f8ab2b3813275d954a8bedcf902d26.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/42f18842ff0c92ed849c4401ae47bb61.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/a8ea64a35799e50ab31ecb65345fe8f4.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/18759fa90cd153bdd744280807c3c155.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/431f00d068a8e747959deb3b7bdd495a.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/5bd3b44f1f4c627bfa39f7809e866ec6.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/7fc36778fe2f6129b9c26e8298c5be7e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c568432e3d5ab6786cd9dcae8008891b.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/ec82ff5aecafa48807117da68cce2ce9.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/b8eccbed570da595e6f8a71ed4abc42c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/9cae1fed6ecefcd61435fe6e2c700fd6.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e306a418f82777399f5e88b93cca22db.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/a66d717084e23864ce079f936557709f.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/6ae06c6505cdbf87a0210fe3b8727d5f.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/58ac2086725b0ba2fe800195f274a0b4.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/5e2e9d9eb099647fbe041ec6645ac034.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/8154357c0fab82bd4e67cda9aaa128c0.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/4325763b738ec3183ecf0d82b2b28e32.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/06d8ea9d10035a00f26c5c52cc601ca7.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/499f30b9e69b5dddf3db36f105756111.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/ed7e1733d54e711a560edb3a76f1a60c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/b6474347eebdb917d2e827fd526dd01c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/b2c0691f9204c5ebc94b4ff678919ca7.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/b4811e702a6884a9251d7cc9e3b06b6f.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/d518783c054695acf329e81d597fdec3.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/835ce09e785cca635c176008975053a1.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/769af780de81a302c0a3b03ed8e6c528.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/da34f99daf9141f0fe56a766461b8485.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/d7c9cd8722a2f9a78e158ce02ec5d4f2.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/09ea18953884b15227819e326103462b.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/5ef728213983842edf1aec27b2c1f5b6.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/95409f2a884dcfeaabfe5e61fcf9ec37.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/6807836dc2a940ba56ea10c7a63b14c9.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e1d976d06853e7a0e6c9cc4ab484ac8a.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/47f5673dec5005092f6d897d6335966c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/1b0109abd0e6a0d13fa2423a96c1167e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/916111a8f94cc0bd39375b3dcac14e35.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c1360df3d6b703c5df9b2f47a2a3d12e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/1d5a31eb93ef873a165993bd99f29df1.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/997a48948b89dd7261ed5a59ba884f45.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/eb96d9689735c9f4019ebf76da43b2b2.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/a92cf2172e6cafe080e4511205568d79.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c9e94570428f197292bb3f43609963f5.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/37145f06cce747311692ad7f276645db.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c9a698b71ed911364fc6f243006c241c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e89db969711efaa441c43d6b90498a0c.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/3803bb1a18229562f18943512b1d3524.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/235cbb5be905ac2b87e7e8f7c8d90144.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/3e38435b3fdbae4ee80b83995592901e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/8ceb7cd3231585da60a74dd2c1aa9015.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e151e225c2e30aab7ccf086094381577.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/363c19306953daf10968f4aa86617997.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/4237a392cf2e69b110ad4ecf35e44059.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/da8ab35ada2dfe55006db01efa96e51a.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/aa1d4fd00b7879db3f1051dc6d16aa87.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/302a8f2d997ff22bedcd837672cdafc2.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/a39ff68c00522aef0472402958a334d2.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/86ccd0eb677c8b552398869d11a8917e.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/a6d0ede352da947060d912d903646a5d.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e6a118bf95bdb61891409d25f193e9c4.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c214066e9bf65d60bcebd691b5b1cbc1.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/c301559ba3ee280bcbf2fc4269bfa9ca.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/573748f5c12ecb4515ba00a7b6e981dc.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/27bcc8bf512a7e6f994a9683b3deea82.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/e22a4507fd1e4b5ef859035e857ae419.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/27b07b4ca709c33ad71b368f87781307.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/ef31eb48bcb133728bffda7e1239b592.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/5f49aaaca59c0733ec92f100d01bc0af.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/818889261deb75044e1018ec53485d85.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><img src=\"http://yanxuan.nosdn.127.net/200369f023243e2faeb18a2a0a352ef1.jpg\" style=\"\" class=\"fr-fin\"><\/p><p><br><\/p>","goodsNumber":100,"goodsSn":"1181000","goodsUnit":"","id":1181000,"isAppExclusive":false,"isDelete":false,"isHot":false,"isLimited":false,"isNew":true,"isOnSale":true,"keywords":"","listPicUrl":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png","marketPrice":2857.8,"name":"母亲节礼物-舒适安睡组合","primaryPicUrl":"https://mp123.oss-cn-shenzhen.aliyuncs.com/upload/20170908/092741972ad4b4.jpg","primaryProductId":1194000,"promotionDesc":"限时购","promotionTag":"","retailPrice":2598,"sellVolume":1533,"sortOrder":1,"unitPrice":0}
     * productList : [{"goodsId":1181000,"goodsNumber":100,"goodsSn":"1181000","goodsSpecificationIds":"3_5_8","id":5,"marketPrice":2857.8,"retailPrice":1598},{"goodsId":1181000,"goodsNumber":100,"goodsSn":"1181000","goodsSpecificationIds":"6_1_7","id":6,"marketPrice":2857.8,"retailPrice":2598}]
     * gallery : [{"goodsId":1181000,"id":677,"imgDesc":"","imgUrl":"http://yanxuan.nosdn.127.net/355efbcc32981aa3b7869ca07ee47dac.jpg","sortOrder":5},{"goodsId":1181000,"id":678,"imgDesc":"","imgUrl":"http://yanxuan.nosdn.127.net/43e283df216881037b70d8b34f8846d3.jpg","sortOrder":5},{"goodsId":1181000,"id":679,"imgDesc":"","imgUrl":"http://yanxuan.nosdn.127.net/12e41d7e5dabaf9150a8bb45c41cf422.jpg","sortOrder":5},{"goodsId":1181000,"id":680,"imgDesc":"","imgUrl":"http://yanxuan.nosdn.127.net/5c1d28e86ccb89980e6054a49571cdec.jpg","sortOrder":5}]
     * issue : [{"answer":"单笔订单金额（不含运费）满88元免邮费；不满88元，每单收取10元运费。\n(港澳台地区需满","id":1,"question":"购买运费如何收取？"},{"answer":"严选默认使用顺丰快递发货（个别商品使用其他快递），配送范围覆盖全国大部分地区（港澳台地区除","id":2,"question":"使用什么快递发货？"},{"answer":"1.自收到商品之日起30日内，顾客可申请无忧退货，退款将原路返还，不同的银行处理时间不同，","id":3,"question":"如何申请退货？"},{"answer":"1.如需开具普通发票，请在下单时选择\u201c我要开发票\u201d并填写相关信息（APP仅限2.4.0及以","id":4,"question":"如何开具发票？"},{"answer":"好！","id":5,"question":"你好吗"}]
     */

    private InfoBean info;
    private List<SpecificationListBean> specificationList;
    private List<ProductListBean> productList;
    private List<GalleryBean> gallery;
    private List<IssueBean> issue;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<SpecificationListBean> getSpecificationList() {
        return specificationList;
    }

    public void setSpecificationList(List<SpecificationListBean> specificationList) {
        this.specificationList = specificationList;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public List<GalleryBean> getGallery() {
        return gallery;
    }

    public void setGallery(List<GalleryBean> gallery) {
        this.gallery = gallery;
    }

    public List<IssueBean> getIssue() {
        return issue;
    }

    public void setIssue(List<IssueBean> issue) {
        this.issue = issue;
    }

    public static class InfoBean {
        /**
         * addTime : 1504064411000
         * appExclusivePrice : 0
         * attributeCategory : 1036000
         * brandId : 1001020
         * categoryId : 1011000
         * counterPrice : 0
         * extraPrice : 0
         * goodsBrief : 安心舒适是最好的礼物
         * goodsDesc : <p><img src="http://yanxuan.nosdn.127.net/3ddfe10db43f7df33ba82ae7570ada80.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/7682b7930b4776ce032f509c24a74a1e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e0bb6a50e27681925c5bb26bceb67ef4.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/ba63b244c74ce06fda82bb6a29cc0f71.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/3c7808c3a4769bad5af4974782f08654.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/71798aaac23a91fdab4d77e1b980a4df.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c88cbb2dd2310b732571f49050fe4059.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/5dfdcd654e0f3076f7c05dd9c19c15ea.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/bd55d6ef7af69422d8d76af10ee70156.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/bae571b22954c521b35e446d652edc1d.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e709c4d9e46d602a4d2125e47110f6ae.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/83e41915035c418db177af8b1eca385c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/f42c561e6935fe3e0e0873653da78670.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/8317726fbae80b98764dc4c6233a913e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/ba904b7c948b8015db2171435325270f.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/4969c73d0d8f29bffb69529c96ca4889.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/d80b9b8c5c31031d1cd5357e48748632.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/3fe79bdae40662a7b1feed3179d3dd1f.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/79eef059963b12479f65e782d1dca128.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e5a8f64f4297ccc01b41df98b0f252c8.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/a940b9e9525c4861407e4c3b07b02977.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/224b8b81cbe12e4ad060a50f1e26601a.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/85e151647452fad718effb7b1adc18e2.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/d47444ff3bb9dc0aa4ab1f9b84d83768.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/136262743f0c849cc0c55c8e7963dd7e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/331a97cbaff5b25a3b08ed7cdfe29df9.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/89b450aa0a8afe1db566dcad926f1fe8.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c1cf94f13b7280a97e751cebe573fa78.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/1822c23def83b77e7607c24237eeec74.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/2af234312b3914d6d0bc316f92134614.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c4f8ab2b3813275d954a8bedcf902d26.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/42f18842ff0c92ed849c4401ae47bb61.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/a8ea64a35799e50ab31ecb65345fe8f4.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/18759fa90cd153bdd744280807c3c155.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/431f00d068a8e747959deb3b7bdd495a.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/5bd3b44f1f4c627bfa39f7809e866ec6.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/7fc36778fe2f6129b9c26e8298c5be7e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c568432e3d5ab6786cd9dcae8008891b.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/ec82ff5aecafa48807117da68cce2ce9.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/b8eccbed570da595e6f8a71ed4abc42c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/9cae1fed6ecefcd61435fe6e2c700fd6.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e306a418f82777399f5e88b93cca22db.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/a66d717084e23864ce079f936557709f.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/6ae06c6505cdbf87a0210fe3b8727d5f.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/58ac2086725b0ba2fe800195f274a0b4.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/5e2e9d9eb099647fbe041ec6645ac034.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/8154357c0fab82bd4e67cda9aaa128c0.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/4325763b738ec3183ecf0d82b2b28e32.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/06d8ea9d10035a00f26c5c52cc601ca7.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/499f30b9e69b5dddf3db36f105756111.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/ed7e1733d54e711a560edb3a76f1a60c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/b6474347eebdb917d2e827fd526dd01c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/b2c0691f9204c5ebc94b4ff678919ca7.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/b4811e702a6884a9251d7cc9e3b06b6f.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/d518783c054695acf329e81d597fdec3.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/835ce09e785cca635c176008975053a1.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/769af780de81a302c0a3b03ed8e6c528.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/da34f99daf9141f0fe56a766461b8485.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/d7c9cd8722a2f9a78e158ce02ec5d4f2.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/09ea18953884b15227819e326103462b.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/5ef728213983842edf1aec27b2c1f5b6.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/95409f2a884dcfeaabfe5e61fcf9ec37.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/6807836dc2a940ba56ea10c7a63b14c9.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e1d976d06853e7a0e6c9cc4ab484ac8a.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/47f5673dec5005092f6d897d6335966c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/1b0109abd0e6a0d13fa2423a96c1167e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/916111a8f94cc0bd39375b3dcac14e35.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c1360df3d6b703c5df9b2f47a2a3d12e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/1d5a31eb93ef873a165993bd99f29df1.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/997a48948b89dd7261ed5a59ba884f45.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/eb96d9689735c9f4019ebf76da43b2b2.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/a92cf2172e6cafe080e4511205568d79.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c9e94570428f197292bb3f43609963f5.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/37145f06cce747311692ad7f276645db.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c9a698b71ed911364fc6f243006c241c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e89db969711efaa441c43d6b90498a0c.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/3803bb1a18229562f18943512b1d3524.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/235cbb5be905ac2b87e7e8f7c8d90144.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/3e38435b3fdbae4ee80b83995592901e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/8ceb7cd3231585da60a74dd2c1aa9015.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e151e225c2e30aab7ccf086094381577.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/363c19306953daf10968f4aa86617997.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/4237a392cf2e69b110ad4ecf35e44059.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/da8ab35ada2dfe55006db01efa96e51a.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/aa1d4fd00b7879db3f1051dc6d16aa87.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/302a8f2d997ff22bedcd837672cdafc2.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/a39ff68c00522aef0472402958a334d2.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/86ccd0eb677c8b552398869d11a8917e.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/a6d0ede352da947060d912d903646a5d.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e6a118bf95bdb61891409d25f193e9c4.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c214066e9bf65d60bcebd691b5b1cbc1.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/c301559ba3ee280bcbf2fc4269bfa9ca.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/573748f5c12ecb4515ba00a7b6e981dc.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/27bcc8bf512a7e6f994a9683b3deea82.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/e22a4507fd1e4b5ef859035e857ae419.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/27b07b4ca709c33ad71b368f87781307.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/ef31eb48bcb133728bffda7e1239b592.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/5f49aaaca59c0733ec92f100d01bc0af.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/818889261deb75044e1018ec53485d85.jpg" style="" class="fr-fin"></p><p><img src="http://yanxuan.nosdn.127.net/200369f023243e2faeb18a2a0a352ef1.jpg" style="" class="fr-fin"></p><p><br></p>
         * goodsNumber : 100
         * goodsSn : 1181000
         * goodsUnit :
         * id : 1181000
         * isAppExclusive : false
         * isDelete : false
         * isHot : false
         * isLimited : false
         * isNew : true
         * isOnSale : true
         * keywords :
         * listPicUrl : http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png
         * marketPrice : 2857.8
         * name : 母亲节礼物-舒适安睡组合
         * primaryPicUrl : https://mp123.oss-cn-shenzhen.aliyuncs.com/upload/20170908/092741972ad4b4.jpg
         * primaryProductId : 1194000
         * promotionDesc : 限时购
         * promotionTag :
         * retailPrice : 2598
         * sellVolume : 1533
         * sortOrder : 1
         * unitPrice : 0
         */

        private long addTime;
        private int appExclusivePrice;
        private int attributeCategory;
        private int brandId;
        private int categoryId;
        private int counterPrice;
        private int extraPrice;
        private String goodsBrief;
        private String goodsDesc;
        private int goodsNumber;
        private String goodsSn;
        private String goodsUnit;
        private int id;
        private boolean isAppExclusive;
        private boolean isDelete;
        private boolean isHot;
        private boolean isLimited;
        private boolean isNew;
        private boolean isOnSale;
        private String keywords;
        private String listPicUrl;
        private double marketPrice;
        private String name;
        private String primaryPicUrl;
        private int primaryProductId;
        private String promotionDesc;
        private String promotionTag;
        private int retailPrice;
        private int sellVolume;
        private int sortOrder;
        private int unitPrice;

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public int getAppExclusivePrice() {
            return appExclusivePrice;
        }

        public void setAppExclusivePrice(int appExclusivePrice) {
            this.appExclusivePrice = appExclusivePrice;
        }

        public int getAttributeCategory() {
            return attributeCategory;
        }

        public void setAttributeCategory(int attributeCategory) {
            this.attributeCategory = attributeCategory;
        }

        public int getBrandId() {
            return brandId;
        }

        public void setBrandId(int brandId) {
            this.brandId = brandId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getCounterPrice() {
            return counterPrice;
        }

        public void setCounterPrice(int counterPrice) {
            this.counterPrice = counterPrice;
        }

        public int getExtraPrice() {
            return extraPrice;
        }

        public void setExtraPrice(int extraPrice) {
            this.extraPrice = extraPrice;
        }

        public String getGoodsBrief() {
            return goodsBrief;
        }

        public void setGoodsBrief(String goodsBrief) {
            this.goodsBrief = goodsBrief;
        }

        public String getGoodsDesc() {
            return goodsDesc;
        }

        public void setGoodsDesc(String goodsDesc) {
            this.goodsDesc = goodsDesc;
        }

        public int getGoodsNumber() {
            return goodsNumber;
        }

        public void setGoodsNumber(int goodsNumber) {
            this.goodsNumber = goodsNumber;
        }

        public String getGoodsSn() {
            return goodsSn;
        }

        public void setGoodsSn(String goodsSn) {
            this.goodsSn = goodsSn;
        }

        public String getGoodsUnit() {
            return goodsUnit;
        }

        public void setGoodsUnit(String goodsUnit) {
            this.goodsUnit = goodsUnit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsAppExclusive() {
            return isAppExclusive;
        }

        public void setIsAppExclusive(boolean isAppExclusive) {
            this.isAppExclusive = isAppExclusive;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public void setIsDelete(boolean isDelete) {
            this.isDelete = isDelete;
        }

        public boolean isIsHot() {
            return isHot;
        }

        public void setIsHot(boolean isHot) {
            this.isHot = isHot;
        }

        public boolean isIsLimited() {
            return isLimited;
        }

        public void setIsLimited(boolean isLimited) {
            this.isLimited = isLimited;
        }

        public boolean isIsNew() {
            return isNew;
        }

        public void setIsNew(boolean isNew) {
            this.isNew = isNew;
        }

        public boolean isIsOnSale() {
            return isOnSale;
        }

        public void setIsOnSale(boolean isOnSale) {
            this.isOnSale = isOnSale;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getListPicUrl() {
            return listPicUrl;
        }

        public void setListPicUrl(String listPicUrl) {
            this.listPicUrl = listPicUrl;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrimaryPicUrl() {
            return primaryPicUrl;
        }

        public void setPrimaryPicUrl(String primaryPicUrl) {
            this.primaryPicUrl = primaryPicUrl;
        }

        public int getPrimaryProductId() {
            return primaryProductId;
        }

        public void setPrimaryProductId(int primaryProductId) {
            this.primaryProductId = primaryProductId;
        }

        public String getPromotionDesc() {
            return promotionDesc;
        }

        public void setPromotionDesc(String promotionDesc) {
            this.promotionDesc = promotionDesc;
        }

        public String getPromotionTag() {
            return promotionTag;
        }

        public void setPromotionTag(String promotionTag) {
            this.promotionTag = promotionTag;
        }

        public int getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(int retailPrice) {
            this.retailPrice = retailPrice;
        }

        public int getSellVolume() {
            return sellVolume;
        }

        public void setSellVolume(int sellVolume) {
            this.sellVolume = sellVolume;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }
    }

    public static class SpecificationListBean {
        /**
         * specification_id : 4
         * valueList : [{"goodsId":1181000,"id":1,"picUrl":"","specificationId":4,"value":"1200g"},{"goodsId":1181000,"id":3,"picUrl":"http://yanxuan.nosdn.127.net/10022c73fa7aa75c2c0d736e96cc56d5.png?quality=90&amp;thumbnail=200x200&amp;imageView","specificationId":4,"value":"500g"}]
         * name : 重量
         */

        private int specification_id;
        private String name;
        private List<ValueListBean> valueList;

        public int getSpecification_id() {
            return specification_id;
        }

        public void setSpecification_id(int specification_id) {
            this.specification_id = specification_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ValueListBean> getValueList() {
            return valueList;
        }

        public void setValueList(List<ValueListBean> valueList) {
            this.valueList = valueList;
        }

        public static class ValueListBean {
            /**
             * goodsId : 1181000
             * id : 1
             * picUrl :
             * specificationId : 4
             * value : 1200g
             */

            private int goodsId;
            private int id;
            private String picUrl;
            private int specificationId;
            private String value;

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public int getSpecificationId() {
                return specificationId;
            }

            public void setSpecificationId(int specificationId) {
                this.specificationId = specificationId;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public static class ProductListBean {
        /**
         * goodsId : 1181000
         * goodsNumber : 100
         * goodsSn : 1181000
         * goodsSpecificationIds : 3_5_8
         * id : 5
         * marketPrice : 2857.8
         * retailPrice : 1598
         */

        private int goodsId;
        private int goodsNumber;
        private String goodsSn;
        private String goodsSpecificationIds;
        private int id;
        private double marketPrice;
        private int retailPrice;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getGoodsNumber() {
            return goodsNumber;
        }

        public void setGoodsNumber(int goodsNumber) {
            this.goodsNumber = goodsNumber;
        }

        public String getGoodsSn() {
            return goodsSn;
        }

        public void setGoodsSn(String goodsSn) {
            this.goodsSn = goodsSn;
        }

        public String getGoodsSpecificationIds() {
            return goodsSpecificationIds;
        }

        public void setGoodsSpecificationIds(String goodsSpecificationIds) {
            this.goodsSpecificationIds = goodsSpecificationIds;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(int retailPrice) {
            this.retailPrice = retailPrice;
        }
    }

    public static class GalleryBean {
        /**
         * goodsId : 1181000
         * id : 677
         * imgDesc :
         * imgUrl : http://yanxuan.nosdn.127.net/355efbcc32981aa3b7869ca07ee47dac.jpg
         * sortOrder : 5
         */

        private int goodsId;
        private int id;
        private String imgDesc;
        private String imgUrl;
        private int sortOrder;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgDesc() {
            return imgDesc;
        }

        public void setImgDesc(String imgDesc) {
            this.imgDesc = imgDesc;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }
    }

    public static class IssueBean {
        /**
         * answer : 单笔订单金额（不含运费）满88元免邮费；不满88元，每单收取10元运费。
         (港澳台地区需满
         * id : 1
         * question : 购买运费如何收取？
         */

        private String answer;
        private int id;
        private String question;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }
}
