import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/views.css?inline';
const $css_0 = typeof $cssFromFile_0  === 'string' ? unsafeCSS($cssFromFile_0) : $cssFromFile_0;
registerStyles('vaadin-grid', $css_0, {moduleId: 'flow_css_mod_0'});
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '59be84b6500fe29b58597096798216f6cab65b9a02eb74a69d0c6a9925375c5e') {
    pending.push(import('./chunks/chunk-efbed2d808eb228981657d9ebc4ac505b814d309765adae77322143df9579ef7.js'));
  }
  if (key === '1c0c0fbea467ecb80ed6fe8c2af3ba640f6e90c6ba8ca9c7818cacf8c7959577') {
    pending.push(import('./chunks/chunk-efbed2d808eb228981657d9ebc4ac505b814d309765adae77322143df9579ef7.js'));
  }
  if (key === 'dfd79aa7b75f6d72f65163c2cc2545b549e4748db106282d1a9925c65e4787cd') {
    pending.push(import('./chunks/chunk-efbed2d808eb228981657d9ebc4ac505b814d309765adae77322143df9579ef7.js'));
  }
  if (key === '493eec5a9771c0ff5307ea50a6d58d5c1aa4d9a05fc6d2b584dd4b47839f834d') {
    pending.push(import('./chunks/chunk-0b0bcf169001179ac193a807a9d9839595427540e5d9eaee6c22358abd30fc12.js'));
  }
  if (key === '520b0178a3a698488c4bc935e19a5f737878be1ea7e7a178edf49b1c87b291f0') {
    pending.push(import('./chunks/chunk-c36b69f1aab925661dcb1781c15494a66312f3a2f769ed2ac38998f5cb70d45f.js'));
  }
  if (key === '397c75c5f11d39305ddf953421411a78b999bc25d4521d5ee50983f33f618a41') {
    pending.push(import('./chunks/chunk-c36b69f1aab925661dcb1781c15494a66312f3a2f769ed2ac38998f5cb70d45f.js'));
  }
  if (key === '01a90516fe9797a830969eb9b201385a89de02c56b061b71c422ff4ecb7fb776') {
    pending.push(import('./chunks/chunk-20837305676b337c66dc6f0d42bf0b7a7f20b3c5fe1db95e6b6f07ece58577df.js'));
  }
  if (key === '24ca1776bab6c3a5c8239b9f8dabdbe9d4788c85ad545251985d4d225d212780') {
    pending.push(import('./chunks/chunk-efbed2d808eb228981657d9ebc4ac505b814d309765adae77322143df9579ef7.js'));
  }
  if (key === '08b19bb5e72b05e5f6da2daccbc8d1f6bb2e5480761e088b1e87295d1c23f257') {
    pending.push(import('./chunks/chunk-efbed2d808eb228981657d9ebc4ac505b814d309765adae77322143df9579ef7.js'));
  }
  if (key === '815fa28083b4e3c87a450a1a4dfc32ad74781edb3812ba47bde494b28d95d920') {
    pending.push(import('./chunks/chunk-ca0e54b0ef0c61b2d8d6599bd7576ab9921264ea25a11f3f1833fb4f5699add6.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}