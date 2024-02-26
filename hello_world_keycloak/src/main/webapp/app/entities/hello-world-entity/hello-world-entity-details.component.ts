import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import HelloWorldEntityService from './hello-world-entity.service';
import { type IHelloWorldEntity } from '@/shared/model/hello-world-entity.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HelloWorldEntityDetails',
  setup() {
    const helloWorldEntityService = inject('helloWorldEntityService', () => new HelloWorldEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const helloWorldEntity: Ref<IHelloWorldEntity> = ref({});

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

    return {
      alertService,
      helloWorldEntity,

      previousState,
      t$: useI18n().t,
    };
  },
});
