import { ref, UnwrapRef } from 'vue'
import { AxiosResponse } from 'axios'
import useLoading from './loading'

// use to fetch list
// Don't use async function. It doesn't work in async function.
// Use the bind function to add parameters
// example: useRequest(api.bind(null, {}))

export default function useRequest<P>(api: () => Promise<AxiosResponse<P>>, defaultValue = [] as unknown as P, isLoading = true) {
  const { loading, setLoading } = useLoading(isLoading)
  const response = ref<P>(defaultValue)
  api()
    .then((res) => {
      response.value = res.data as UnwrapRef<P>
    })
    .finally(() => {
      setLoading(false)
    })
  return { loading, response }
}
