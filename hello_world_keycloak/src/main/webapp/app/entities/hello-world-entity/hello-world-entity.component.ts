import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import HelloWorldEntityService from './hello-world-entity.service';
import { type IHelloWorldEntity } from '@/shared/model/hello-world-entity.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HelloWorldEntity',
  setup() {
    const { t: t$ } = useI18n();
    const helloWorldEntityService = inject('helloWorldEntityService', () => new HelloWorldEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const helloWorldEntities: Ref<IHelloWorldEntity[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveHelloWorldEntitys = async () => {
      isFetching.value = true;
      try {
        const res = await helloWorldEntityService().retrieve();
        helloWorldEntities.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveHelloWorldEntitys();
    };

    onMounted(async () => {
      await retrieveHelloWorldEntitys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IHelloWorldEntity) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeHelloWorldEntity = async () => {
      try {
        await helloWorldEntityService().delete(removeId.value);
        const message = t$('helloWorldKeycloakApp.helloWorldEntity.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveHelloWorldEntitys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      helloWorldEntities,
      handleSyncList,
      isFetching,
      retrieveHelloWorldEntitys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeHelloWorldEntity,
      t$,
    };
  },
});
