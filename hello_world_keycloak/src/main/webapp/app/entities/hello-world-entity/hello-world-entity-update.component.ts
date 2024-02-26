import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import HelloWorldEntityService from './hello-world-entity.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IHelloWorldEntity, HelloWorldEntity } from '@/shared/model/hello-world-entity.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HelloWorldEntityUpdate',
  setup() {
    const helloWorldEntityService = inject('helloWorldEntityService', () => new HelloWorldEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const helloWorldEntity: Ref<IHelloWorldEntity> = ref(new HelloWorldEntity());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveHelloWorldEntity = async helloWorldEntityId => {
      try {
        const res = await helloWorldEntityService().find(helloWorldEntityId);
        helloWorldEntity.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.helloWorldEntityId) {
      retrieveHelloWorldEntity(route.params.helloWorldEntityId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
      testValue: {},
    };
    const v$ = useVuelidate(validationRules, helloWorldEntity as any);
    v$.value.$validate();

    return {
      helloWorldEntityService,
      alertService,
      helloWorldEntity,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.helloWorldEntity.id) {
        this.helloWorldEntityService()
          .update(this.helloWorldEntity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('helloWorldKeycloakApp.helloWorldEntity.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.helloWorldEntityService()
          .create(this.helloWorldEntity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('helloWorldKeycloakApp.helloWorldEntity.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
